package com.warmthdawn.zenscript.annotator

import com.intellij.analysis.AnalysisBundle
import com.intellij.codeInspection.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.warmthdawn.zenscript.bundle.ZenScriptBundle
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.reference.ZenResolveResultType.*
import com.warmthdawn.zenscript.type.*
import com.warmthdawn.zenscript.util.MethodMetadata
import com.warmthdawn.zenscript.util.extractMethodMetadata

private val logger = Logger.getInstance(ZenScriptAnnotator::class.java)

class ZenScriptAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        if (element is ZenScriptReference) {
            if (!checkRef(element)) {
                generateNoReferenceError(element, holder)
            }
        }

        if (element is ZenScriptCallExpression) {
            checkCall(element, holder)
        }
    }

    private fun checkCall(element: ZenScriptCallExpression, holder: AnnotationHolder) {
        val methodExpr = element.expression
        var methodType: ZenType? = null
        if (methodExpr is ZenScriptReference) {
            val resolveResults = methodExpr.advancedResolve()
            val candidateMethods = resolveResults.filter { it.type == ZEN_METHOD || it.type == JAVA_METHODS }
            if (resolveResults.isNotEmpty() && candidateMethods.isEmpty()) {
                val resolve = resolveResults[0]
                when (resolve.type) {

                    ZEN_CLASS, JAVA_CLASS -> {
                        generateNoConstructorError(methodExpr, holder)
                        return
                    }

                    ZEN_VARIABLE, JAVA_PROPERTY, JAVA_GLOBAL_VAR -> {
                        methodType = getTargetType(resolveResults)
                    }

                    JAVA_GLOBAL_FUNCTION -> {
                        checkMethodCall(resolve.element as PsiMethod, element.arguments, holder)
                        return
                    }

                    else -> {
                        logger.error("${resolve.type} is not supported in call resolve!")
                        return
                    }
                }
            } else if (candidateMethods.size == 1) {
                if (!candidateMethods[0].isValidResult) {
                    when (val method = candidateMethods[0].element) {
                        is PsiMethod ->
                            checkMethodCall(method, element.arguments, holder)

                        is ZenScriptFunctionDeclaration, is ZenScriptExpandFunctionDeclaration, is ZenScriptConstructorDeclaration ->
                            checkMethodCall(method as ZenScriptFunction, element.arguments, holder)

                        else -> logger.error("$element is not supported in call resolve!")
                    }
                }
                return
            } else if (candidateMethods.size > 1) {
                checkMethodCallMulti(
                    methodExpr,
                    candidateMethods.map { it.element },
                    element.arguments,
                    holder
                )
                return
            }
        } else {
            methodType = getType(methodExpr)
        }
        if (methodType !is ZenScriptFunctionType) {
            generateNotCallable(methodExpr, holder)
            return
        }
        checkMethodCall(methodType, methodExpr.text, element.arguments, holder)

    }

    enum class ArgumentCheckResult {
        MISSING,
        TOO_MANY,
        TYPE_MISMATCH,
        UNKNOWN_TYPE,
        SUCCESS,
    }

    private fun checkMethodArguments(
        arguments: ZenScriptArguments,
        parameterTypes: List<ZenType>,
        firstOptionalIndex: Int,
        isVarargs: Boolean,
    ): Array<ArgumentCheckResult> {
        val typeService = ZenScriptTypeService.getInstance(arguments.project)
        val argumentList = arguments.expressionList
        val argSize = argumentList.size
        val result = Array(argSize + 1) { ArgumentCheckResult.SUCCESS }
        for ((i, paramsType) in parameterTypes.withIndex()) {
            if (i >= argSize) {
                if (firstOptionalIndex < 0 || i < firstOptionalIndex) {
                    result[argSize] = ArgumentCheckResult.MISSING
                }
                break
            }
            val arg = argumentList[i]
            val argType = getType(arg)

            if (argType is ZenUnknownType) {
                result[i] = ArgumentCheckResult.UNKNOWN_TYPE
            } else if (!typeService.canCast(paramsType, argType)) {
                result[i] = ArgumentCheckResult.TYPE_MISMATCH
            }
        }

        if (argSize > parameterTypes.size) {
            if (!isVarargs) {
                for (i in parameterTypes.size until argSize) {
                    result[i] = ArgumentCheckResult.TOO_MANY
                }
            } else {
                val varargType = parameterTypes.last()
                for (i in parameterTypes.size until argSize) {
                    val arg = argumentList[i]
                    val argType = getType(arg)
                    if (argType is ZenUnknownType) {
                        result[i] = ArgumentCheckResult.UNKNOWN_TYPE
                    } else if (!typeService.canCast(varargType, argType)) {
                        result[i] = ArgumentCheckResult.TYPE_MISMATCH
                    }
                }
            }
        }

        return result
    }

    private fun checkMethodCall(
        methodType: ZenScriptFunctionType,
        methodName: String,
        arguments: ZenScriptArguments,
        holder: AnnotationHolder,
    ) {
        doCheckMethodCall(methodType.extractMethodMetadata(), methodName, arguments, holder)
    }


    private fun checkMethodCall(
        method: ZenScriptFunction,
        arguments: ZenScriptArguments,
        holder: AnnotationHolder,
    ) {
        val parameters = method.parameters?.parameterList
        if (!method.isValid || parameters == null) {
            return
        }
        val name = (method as? ZenScriptNamedElement)?.name

        doCheckMethodCall(method.extractMethodMetadata(), name, arguments, holder)

    }

    private fun checkMethodCall(
        method: PsiMethod,
        arguments: ZenScriptArguments,
        holder: AnnotationHolder,
    ) {
        doCheckMethodCall(method.extractMethodMetadata(), method.name, arguments, holder)
    }


    private fun checkMethodCallMulti(
        methodExpr: ZenScriptExpression,
        method: List<PsiElement>,
        arguments: ZenScriptArguments,
        holder: AnnotationHolder,
    ) {
        val name = if (methodExpr is ZenScriptReference) methodExpr.canonicalText else methodExpr.text
        doCheckMultiMethodCall(
            methodExpr,
            method.filter { it.isValid }
                .mapNotNull {
                    when (it) {
                        is PsiMethod -> it.extractMethodMetadata()
                        is ZenScriptFunction -> it.extractMethodMetadata()
                        else -> null
                    }
                },
            name,
            arguments,
            holder
        )

    }

    private fun doCheckMethodCall(
        method: MethodMetadata, name: String?, arguments: ZenScriptArguments,
        holder: AnnotationHolder,
    ) {
        val (parameterTypes, parameterNames, firstOptionalIndex, isVarargs) = method
        val checkResults = checkMethodArguments(arguments, parameterTypes, firstOptionalIndex, isVarargs)
        val missingCheck = checkResults.last()
        val argsList = arguments.expressionList
        if (missingCheck == ArgumentCheckResult.MISSING) {
            val namesSequence = parameterNames?.asSequence() ?: parameterTypes.asSequence().map { it.displayName }
            val missing = if (firstOptionalIndex >= 0) {
                namesSequence.take(firstOptionalIndex).drop(argsList.size).toList()
            } else {
                namesSequence.drop(argsList.size).toList()
            }
            generateMissingArgumentsError(arguments, holder, missing)
        }
        for ((i, result) in checkResults.asSequence().take(checkResults.size - 1).withIndex()) {
            if (result == ArgumentCheckResult.SUCCESS) {
                continue
            }
            val errorExpr = argsList[i]
            val expected =
                if (i < parameterTypes.size) parameterNames?.get(i) to parameterTypes[i] else null
            generateCallError(errorExpr, expected, name, holder, result)
        }
    }

    private fun doCheckMultiMethodCall(
        methodExpr: ZenScriptExpression,
        methods: List<MethodMetadata>, name: String,
        arguments: ZenScriptArguments,
        holder: AnnotationHolder
    ) {

        val candidates = mutableListOf<Pair<Boolean, List<Pair<Boolean, ZenType>>>>();
        for (method in methods) {
            val (parameterTypes, _, firstOptionalIndex, isVarargs) = method
            val checkResults = checkMethodArguments(arguments, parameterTypes, firstOptionalIndex, isVarargs)
            val argInfo = mutableListOf<Pair<Boolean, ZenType>>()
            val argsList = arguments.expressionList

            for ((i, paramType) in parameterTypes.withIndex()) {
                val match = checkResults[i.coerceAtMost(checkResults.size - 1)]
                argInfo.add((match == ArgumentCheckResult.SUCCESS) to paramType)
            }
            candidates.add((argsList.size > parameterTypes.size && !isVarargs) to argInfo)

        }
        generateErrorCallMulti(
            methodExpr,
            name,
            candidates,
            holder
        )
    }

    private fun checkRef(ref: ZenScriptReference): Boolean {
        if (ref is ZenScriptMemberAccessExpression) {
            val qualifierType = getType(ref.expression)
            if (qualifierType == ZenPrimitiveType.ANY) {
                return true
            }
            if (qualifierType is ZenScriptPackageType) {
                return getType(ref) !is ZenUnknownType
            }
        } else if (ref is ZenScriptLocalAccessExpression) {
            val type = getType(ref)
            if (type is ZenScriptPackageType) {
                return true
            }
        }
        val result = ref.advancedResolve()
        return result.isNotEmpty()
    }

    private fun generateMissingArgumentsError(
        arguments: ZenScriptArguments,
        holder: AnnotationHolder,
        missing: List<String>
    ) {
        val range = if (arguments.expressionList.isEmpty()) {
            arguments.textRange
        } else {
            arguments.lastChild.textRange
        }
        for (name in missing) {
            val message = ZenScriptBundle.message("error.call.missing", name)
            holder.newAnnotation(HighlightSeverity.ERROR, message)
                .range(range)
                .highlightType(ProblemHighlightType.GENERIC_ERROR)
                .create()
        }
    }

    private fun generateErrorCallMulti(
        expression: ZenScriptExpression,
        methodName: String,
        candidates: List<Pair<Boolean, List<Pair<Boolean, ZenType>>>>,
        holder: AnnotationHolder
    ) {
        val range = if (expression is ZenScriptReference) expression.rangeInElement.let {
            TextRange.from(
                expression.element.textRange.startOffset
                        + it.startOffset, it.length
            )
        } else expression.textRange
        val message = ZenScriptBundle.message("error.call.multi.error", methodName)
        val tooltip = buildString {
            append("<html><body>")
            append(message)

            append("<br>")
            append("<ul>")
            for ((isTooMany, candidate) in candidates) {
                append("<li>")
                append(methodName)
                append("(")
                for ((i, pair) in candidate.withIndex()) {
                    val (match, zenType) = pair
                    if (!match) {
                        append("<b style='color:red'>")
                    }
                    append(zenType.displayName)
                    if (!match) {
                        append("</b>")
                    }
                    if (i != candidate.size - 1) {
                        append(", ")
                    }
                }
                if (isTooMany) {
                    append("<b style='color:red'>")
                }
                append(")")
                if (isTooMany) {
                    append("</b>")
                }
                append("</li>")
            }
            append("</ul>")
            append("</body></html>")
        }

        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .tooltip(tooltip)
            .range(range)
            .highlightType(ProblemHighlightType.GENERIC_ERROR)
            .create()
    }

    private fun generateCallError(
        expression: ZenScriptExpression,
        expected: Pair<String?, ZenType>?,
        methodName: String?,
        holder: AnnotationHolder,
        checkResult: ArgumentCheckResult
    ) {
        val range = expression.textRange

        val message = when (checkResult) {
            ArgumentCheckResult.UNKNOWN_TYPE -> ZenScriptBundle.message("error.call.type.unknown", expression.text)
            ArgumentCheckResult.TOO_MANY -> {
                if (methodName != null) {
                    ZenScriptBundle.message("error.call.argument.too.many", methodName)
                } else {
                    ZenScriptBundle.message("error.call.argument.too.many.unnamed")
                }
            }

            ArgumentCheckResult.TYPE_MISMATCH -> {
                if (expected!!.first == null) {

                    ZenScriptBundle.message(
                        "error.call.type.mismatch.unnamed",
                        expected.second.displayName,
                        getType(expression).displayName
                    )
                } else {
                    ZenScriptBundle.message(
                        "error.call.type.mismatch",
                        expected.first!!,
                        expected.second.displayName,
                        getType(expression).displayName
                    )
                }
            }

            else -> throw IllegalArgumentException("not supported: $checkResult")
        }


        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .range(range)
            .highlightType(ProblemHighlightType.GENERIC_ERROR)
            .create()
    }

    private fun generateNotCallable(methodExpr: ZenScriptExpression, holder: AnnotationHolder) {
        val range = methodExpr.textRange

        val message = ZenScriptBundle.message("error.call.expression", methodExpr.text)

        val builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
            .range(range)
            .highlightType(ProblemHighlightType.GENERIC_ERROR)

        builder.create()
    }

    private fun generateNoConstructorError(classRef: ZenScriptReference, holder: AnnotationHolder) {
        val rangeInElement = classRef.rangeInElement

        val range = TextRange.from(
            classRef.element.textRange.startOffset
                    + rangeInElement.startOffset, rangeInElement.length
        )

        val message = ZenScriptBundle.message("error.call.interface", classRef.canonicalText)

        val builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
            .range(range)
            .highlightType(ProblemHighlightType.GENERIC_ERROR)

        builder.create()
    }

    private fun generateNoReferenceError(ref: ZenScriptReference, holder: AnnotationHolder) {
        val rangeInElement = ref.rangeInElement

        val range = TextRange.from(
            ref.element.textRange.startOffset
                    + rangeInElement.startOffset, rangeInElement.length
        )

        val message = AnalysisBundle.message("cannot.resolve.symbol", ref.canonicalText)

        var builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
            .range(range)
            .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)

        if (ref is LocalQuickFixProvider) {
            val fixes = ref.quickFixes
            if (fixes != null) {
                val inspectionManager = InspectionManager.getInstance(ref.element.project)
                for (fix in fixes) {
                    val descriptor = inspectionManager.createProblemDescriptor(
                        ref.element, message, fix,
                        ProblemHighlightType.LIKE_UNKNOWN_SYMBOL, true
                    )
                    builder = builder.newLocalQuickFix(fix, descriptor).registerFix()
                }
            }
        }

        builder.create()
    }

}