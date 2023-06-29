package com.warmthdawn.zenscript.codeInsight

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.lang.parameterInfo.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parents
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.reference.ZenResolveResultType
import com.warmthdawn.zenscript.reference.resolveZenScriptReference
import com.warmthdawn.zenscript.type.*
import com.warmthdawn.zenscript.util.argumentTypes
import com.warmthdawn.zenscript.util.returnType
import com.warmthdawn.zenscript.util.type
import java.lang.IllegalArgumentException


class MethodCandidate(
    val kind: Kind,
    val element: PsiElement?
) {
    enum class Kind {
        JAVA_FUNCTION,
        JAVA_EXPAND_FUNCTION,
        JAVA_CONSTRUCTOR,
        ZEN_FUNCTION,
        ZEN_EXPAND_FUNCTION,
        ZEN_CONSTRUCTOR,
        TYPE_FUNCTION
    }

    var parameters: List<Triple<String, ZenType, String?>> = emptyList()
        private set

    var returnType: ZenType? = null
        private set

    var isVarargs: Boolean = false
        private set

    constructor(element: PsiElement, kind: Kind) : this(kind, element) {
        when (kind) {
            Kind.JAVA_FUNCTION, Kind.JAVA_EXPAND_FUNCTION, Kind.JAVA_CONSTRUCTOR -> {
                element as PsiMethod
                var parameters = element.parameterList.parameters.asSequence()

                if (kind == Kind.JAVA_EXPAND_FUNCTION && element.parameterList.parametersCount > 0) {
                    parameters = parameters.drop(1)
                }

                this.parameters = parameters.map {
                    val name = it.name
                    val type = ZenType.fromJavaType(it.type)
                    val optionalDefault = it.getAnnotation("stanhebben.zenscript.annotations.Optional")?.let { anno ->
                        val attrValue = anno.findDeclaredAttributeValue("valueLong")
                            ?: anno.findDeclaredAttributeValue("valueBoolean")
                            ?: anno.findDeclaredAttributeValue("valueDouble")
                            ?: anno.findDeclaredAttributeValue("value")
                        if (attrValue == null) {
                            "..."
                        } else {
                            JavaPsiFacade.getInstance(anno.project).constantEvaluationHelper
                                .computeConstantExpression(attrValue)
                                .toString()
                        }
                    }

                    Triple(name, type, optionalDefault)
                }.toList()

                returnType = element.returnType?.let { ZenType.fromJavaType(it) }
                isVarargs = element.isVarArgs
            }

            Kind.ZEN_FUNCTION, Kind.ZEN_EXPAND_FUNCTION, Kind.ZEN_CONSTRUCTOR -> {
                element as ZenScriptFunction
                parameters = element.parameters?.parameterList?.map {
                    val name = it.name ?: "arg"
                    val type = it.type

                    val optionalDefault: String? = it.initializer?.expression?.let { init ->
                        if (init is ZenScriptPrimitiveLiteral) {
                            init.text
                        } else {
                            "..."
                        }
                    }

                    Triple(name, type, optionalDefault)
                } ?: emptyList()

                if (kind != Kind.ZEN_CONSTRUCTOR) {
                    returnType = element.returnType
                }
            }

            Kind.TYPE_FUNCTION -> {
                throw IllegalArgumentException("type function should use another ctor")
            }
        }
    }

    constructor(functionType: ZenScriptFunctionType, kind: Kind) : this(kind, null) {
        if (kind != Kind.TYPE_FUNCTION) {
            throw IllegalArgumentException()
        }

        if (functionType is ZenScriptFunctionTypeNamedParameters) {

            val parameterNames = functionType.parameterNames
            parameters = functionType.parameterTypes.mapIndexed { index: Int, zenType: ZenType ->
                val isOptional = index >= functionType.firstOptionalIndex
                Triple(parameterNames[index], zenType, if (isOptional) "..." else null)
            }
            isVarargs = functionType.isVarargs
        } else {
            parameters = functionType.parameterTypes.mapIndexed { index: Int, zenType: ZenType ->
                Triple("arg$index", zenType, null)
            }
        }
        returnType = functionType.returnType
    }

}

class ZenScriptParameterInfoHandler :
    ParameterInfoHandler<ZenScriptArguments, MethodCandidate> {

    companion object {
        val logger = Logger.getInstance(this::class.java)
    }

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): ZenScriptArguments? {
        val file = context.file as? ZenScriptFile ?: return null
        val token = file.findElementAt(context.offset) ?: return null

        val arguments = PsiTreeUtil.getParentOfType(token, ZenScriptArguments::class.java, true)
            ?: throw NullPointerException("could not find containing call expr of argument at ${context.offset}")

        val overloads = findOverloadMethods(arguments)
        if (overloads.isEmpty()) {
            return null
        }

        context.itemsToShow = overloads

        return arguments
    }

    private fun findOverloadMethods(arguments: ZenScriptArguments): Array<MethodCandidate> {
        val call = arguments.parent as ZenScriptCallExpression
        val project = arguments.project
        val methodExpr = call.expression
        var methodType: ZenType? = null
        if (methodExpr is ZenScriptReference) {

            val resolvedMethods = resolveZenScriptReference(methodExpr, incompleteCode = true, filterMethods = false)

            if (resolvedMethods.isNotEmpty()) {

                val result = resolvedMethods.asSequence()
                    .filter { it.type == ZenResolveResultType.ZEN_METHOD || it.type == ZenResolveResultType.JAVA_METHODS }
                    .map { it.element }
                    .map {

                        when (it) {
                            is PsiMethod -> {
                                if (it.isConstructor) {
                                    logger.error("unexpected resolve: find constructors in raw method candidates: ${call.text}")
                                }
                                val isExpand = false // TODO implement expand
                                if (isExpand) {
                                    MethodCandidate(it, MethodCandidate.Kind.JAVA_EXPAND_FUNCTION)
                                } else {
                                    MethodCandidate(it, MethodCandidate.Kind.JAVA_FUNCTION)
                                }
                            }

                            is ZenScriptFunctionDeclaration -> {
                                MethodCandidate(it, MethodCandidate.Kind.ZEN_FUNCTION)
                            }

                            is ZenScriptExpandFunctionDeclaration -> {
                                MethodCandidate(it, MethodCandidate.Kind.ZEN_EXPAND_FUNCTION)
                            }

                            else -> {
                                throw IllegalStateException("unexpected resolve: find invalid kind in raw method candidates: ${call.text}")
                            }
                        }
                    }
                    .toList()
                    .toTypedArray()

                if (result.isNotEmpty()) {
                    return result
                }

                if (resolvedMethods.size != 1) {
                    logger.error("expression \"${call.text} resolved multiple non-method result!\"")
                }
                when (resolvedMethods[0].type) {
                    ZenResolveResultType.ZEN_CLASS -> {
                        return (resolvedMethods[0].element as ZenScriptClassDeclaration)
                            .constructors
                            .map {
                                MethodCandidate(it, MethodCandidate.Kind.ZEN_CONSTRUCTOR)
                            }
                            .toTypedArray()
                    }

                    ZenResolveResultType.JAVA_CLASS -> {
                        return (resolvedMethods[0].element as PsiClass)
                            .constructors
                            .map {
                                MethodCandidate(it, MethodCandidate.Kind.JAVA_CONSTRUCTOR)
                            }
                            .toTypedArray()
                    }

                    ZenResolveResultType.ZEN_VARIABLE, ZenResolveResultType.JAVA_PROPERTY, ZenResolveResultType.JAVA_GLOBAL_VAR, ZenResolveResultType.JAVA_GLOBAL_FUNCTION -> {
                        methodType = getTargetType(resolvedMethods)
                    }

                    else -> {
                        logger.error("unexpected \"${call.text} with type ${resolvedMethods[0].type} resolved for call expr!\"")
                    }
                }

            }

        }

        if (methodType == null) {
            methodType = getType(methodExpr)
        }

        if (methodType is ZenScriptClassType && methodType.isLibrary) {
            val javaClazz = findJavaClass(project, methodType)
            val functionalInterface = getFunctionalInterfaceMethod(javaClazz)
            if (functionalInterface != null) {
                methodType = ZenScriptFunctionType.fromJavaMethod(functionalInterface)
            }
        }

        if (methodType is ZenScriptFunctionType) {
            return arrayOf(MethodCandidate(methodType, MethodCandidate.Kind.TYPE_FUNCTION))
        }

        // not callable
        return emptyArray()
    }


    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): ZenScriptArguments? {
        val token = context.file.findElementAt(context.offset) ?: return null

        val argumentList = PsiTreeUtil.getParentOfType(token, ZenScriptArguments::class.java, true)
            ?: return null

        val argument = token.parents(false).takeWhile { it != argumentList }.lastOrNull()

        if (argument is ZenScriptExpression) {
            val arguments = argumentList.expressionList
            var index = arguments.indexOf(argument)
            if (index < 0) {
                index = 0
            }
            context.setCurrentParameter(index)
        }


        val candidates = context.objectsToView

        val typeUtil = ZenScriptTypeService.getInstance(context.project)
        val argumentTypes = argumentList.argumentTypes
        var bestPriority = ZenCallPriority.INVALID
        for ((i, candidate) in candidates.withIndex()) {
            if (candidate !is MethodCandidate) {
                continue
            }
            val parameterTypes = candidate.parameters.map { it.second }
            val firstOptionalIndex = candidate.parameters.indexOfFirst { it.third != null }
            val callPriority =
                typeUtil.getCallPriority(argumentTypes, parameterTypes, firstOptionalIndex, candidate.isVarargs)

            if (callPriority.priority > bestPriority.priority) {
                bestPriority = callPriority
                context.highlightedParameter = candidate
            }
            context.setUIComponentEnabled(i, callPriority != ZenCallPriority.INVALID)

        }

        return argumentList

    }

    override fun updateUI(candidate: MethodCandidate, context: ParameterInfoUIContext) {
        val enabled = context.isUIComponentEnabled
        val (text, currentParameterStart, currentParameterEnd) = buildPresentation(
            candidate,
            context.currentParameterIndex,
                    enabled
        )

        context.setupUIComponentPresentation(
            text, currentParameterStart, currentParameterEnd, !enabled,
            false/*strikeout*/, false/*isDisabledBeforeHighlight*/,
            context.defaultParameterColor
        )

    }

    private fun buildPresentation(
        candidateInfo: MethodCandidate,
        currentIndex: Int,
        enabled: Boolean
    ): Triple<String, Int, Int> {
        val settings = CodeInsightSettings.getInstance()
        var currentParameterStart = -1
        var currentParameterEnd = -1


        val parameters: List<Triple<String, ZenType, String?>> = candidateInfo.parameters

        val text = buildString {
            if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
                if (candidateInfo.element is PsiNamedElement) {
                    append(candidateInfo.element.name)
                } else {
                    append("<call>")
                }
                append("(")
            }
            if (parameters.isEmpty()) {
                append(CodeInsightBundle.message("parameter.info.no.parameters"))
            } else {

                val currentParameter = if (candidateInfo.isVarargs && currentIndex >= parameters.size) {
                    parameters.size - 1
                } else {
                    currentIndex
                }
                for ((i, entry) in parameters.withIndex()) {
                    val (name, type, default) = entry
                    val startIndex = length
                    append(name)
                    append(" as ")
                    append(type.displayName)
                    if (default != null) {
                        append(" = ")
                        append(default)
                    }
                    if (i == currentParameter && enabled) {
                        currentParameterStart = startIndex
                        currentParameterEnd = length
                    }
                    if (i != parameters.size - 1) {
                        append(", ")
                    }
                }
            }

            if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
                append(")")
                if (candidateInfo.kind != MethodCandidate.Kind.ZEN_CONSTRUCTOR && candidateInfo.kind !== MethodCandidate.Kind.JAVA_CONSTRUCTOR) {
                    if (candidateInfo.returnType != null) {
                        append(" as ")
                        append(candidateInfo.returnType!!.displayName)
                    }
                }
            }
        }


        return Triple(text, currentParameterStart, currentParameterEnd)
    }


    override fun updateParameterInfo(argumentList: ZenScriptArguments, context: UpdateParameterInfoContext) {
        if (context.parameterOwner != argumentList) {
            context.removeHint()
        }
        val offset = context.offset

        val children = sequence {
            var current = argumentList.firstChild
            val end = argumentList.lastChild
            while (current != null && current != end) {
                yield(current)
                current = current.nextSibling
            }
        }
        val parameterIndex = children
            .takeWhile { it.node.startOffset < offset }
            .count { it.node.elementType == ZenScriptTypes.COMMA }
        context.setCurrentParameter(parameterIndex)
    }

    override fun showParameterInfo(element: ZenScriptArguments, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset, this)
    }

    private fun findArguments() {

    }

}