package com.warmthdawn.zenscript.type

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.elementType
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.index.ZenScriptClassNameIndex
import com.warmthdawn.zenscript.index.ZenScriptMemberCache
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.reference.ZenResolveResultType
import com.warmthdawn.zenscript.reference.ZenScriptElementResolveResult


fun getTargetType(resolveResults: Array<ZenScriptElementResolveResult>, skipMethods: Boolean = true): ZenType? {
    if (resolveResults.isEmpty()) {
        return null
    }

    for (resolveResult in resolveResults) {
        val element = resolveResult.element
        when (resolveResult.type) {
            ZenResolveResultType.JAVA_PROPERTY -> {
                if (element is PsiField) {
                    return ZenType.fromJavaType(element.type)
                }
                if (element is PsiMethod) {
                    val params = element.parameterList
                    if (params.parametersCount == 0) {
                        return ZenType.fromJavaType(element.returnType)
                    }
                    if (params.parametersCount == 1) {
                        return ZenType.fromJavaType(params.getParameter(0)!!.type)
                    }
                }
            }

            ZenResolveResultType.ZEN_CLASS -> {
                element as ZenScriptClassDeclaration
                val file = element.containingFile as ZenScriptFile
                ZenScriptClassType(file.packageName + "." + element.qualifiedName!!.text)
            }

            ZenResolveResultType.JAVA_CLASS -> {
                element as PsiClass
                val qualifiedName = element.getAnnotation("stanhebben.zenscript.annotations.ZenClass")?.let { anno ->
                    AnnotationUtil.getStringAttributeValue(anno, "value")
                }
                return if (qualifiedName != null)
                    ZenScriptClassType(qualifiedName)
                else ZenUnknownType(element.qualifiedName!!)
            }

            ZenResolveResultType.ZEN_METHOD -> {
                if (skipMethods) {
                    continue
                }
                return ZenPrimitiveType.ANY // TODO: Function
            }

            ZenResolveResultType.JAVA_METHODS -> {
                if (skipMethods) {
                    continue
                }
                return ZenPrimitiveType.ANY // TODO: Function
            }

            ZenResolveResultType.JAVA_GLOBAL_VAR -> {
                if (element is PsiField) {
                    return ZenType.fromJavaType(element.type)
                }
                // TODO: Function
                return ZenPrimitiveType.ANY

            }

            ZenResolveResultType.ZEN_VARIABLE -> {
                return getVariableType((element as ZenScriptVariableDeclaration))
            }

            else -> throw IllegalArgumentException()
        }

    }

    return ZenUnknownType("<unknown>")
}

private fun getVariableType(decl: ZenScriptVariableDeclaration): ZenType {
    val typeRef = decl.typeRef
    val initializer = decl.initializer?.expression

    return if (typeRef == null && initializer == null) {
        ZenPrimitiveType.ANY
    } else if (typeRef != null) {
        ZenType.fromTypeRef(typeRef)
    } else {
        getType(initializer)
    }
}

fun getType(expr: ZenScriptExpression?): ZenType {
    return when (expr) {
        is ZenScriptLocalAccessExpression -> getTypeImpl(expr)
        is ZenScriptMemberAccessExpression -> getTypeImpl(expr)
        is ZenScriptCallExpression -> getTypeImpl(expr)
        is ZenScriptArrayIndexExpression -> getTypeImpl(expr)
        is ZenScriptInstanceOfExpression -> ZenPrimitiveType.BOOL
        is ZenScriptTypeCastExpression -> ZenType.fromTypeRef(expr.typeRef)
        is ZenScriptLiteralExpression -> getTypeImpl(expr)
        is ZenScriptParenExpression -> getType(expr.expression)
        // ops

        is ZenScriptBinaryExpression -> getTypeImpl(expr)
        is ZenScriptUnaryExpression -> getTypeImpl(expr)
        is ZenScriptConditionalExpression -> getTypeImpl(expr)

        null -> ZenUnknownType("<unknown>")
        else -> ZenUnknownType(expr.text)
    }
}

fun getTypeImpl(expr: ZenScriptUnaryExpression): ZenType {
    return getType(expr)
}

fun getTypeImpl(expr: ZenScriptConditionalExpression): ZenType {
    return getType(expr.truePart)
}

fun getTypeImpl(expr: ZenScriptBinaryExpression): ZenType {
    // TODO
    return getType(expr.left)
}

fun getTypeImpl(expr: ZenScriptArrayIndexExpression): ZenType {
    // TODO
    val mapType = getType(expr.expression)
    if (mapType is ZenScriptMapType) {
        return mapType.valueType
    }
    if (mapType is ZenScriptArrayType) {
        return mapType.elementType
    }
    if (mapType is ZenScriptListType) {
        return mapType.elementType
    }
    return ZenUnknownType("<unknown>")
}

fun getTypeImpl(expr: ZenScriptCallExpression): ZenType {
    val methodExpr = expr.expression
    val project = expr.project
    val typeUtil = ZenScriptTypeService.getInstance(project)
    val arguments = expr.arguments.expressionList.map { getType(it) }
    var methodType: ZenType? = null
    if (methodExpr is ZenScriptReference) {

        val resolvedMethods = methodExpr.advancedResolve(false)

        if (resolvedMethods.isNotEmpty()) {

            val candidateMethods = resolvedMethods.asSequence()
                    .filter { it.type == ZenResolveResultType.ZEN_METHOD || it.type == ZenResolveResultType.JAVA_METHODS }
                    .map { it.element }
                    .toList()
            if (candidateMethods.isNotEmpty()) {

                val selected = typeUtil.selectMethod(arguments, candidateMethods)

                if (selected < 0) {
                    return ZenUnknownType("<unknown call>")
                }
                val method = candidateMethods[selected]

                return if (method is ZenScriptFunctionDeclaration) {
                    // TODO: predicate type
                    method.returnType?.let { ZenType.fromTypeRef(it) } ?: ZenPrimitiveType.ANY
                } else {
                    ZenType.fromJavaType((method as PsiMethod).returnType!!)
                }

            }

            if (resolvedMethods.isNotEmpty()) {
                when (resolvedMethods[0].type) {
                    ZenResolveResultType.ZEN_CLASS -> {
                        return getTargetType(resolvedMethods, true)!!
                    }

                    ZenResolveResultType.JAVA_CLASS -> {
                        return getTargetType(resolvedMethods, true)!!
                    }

                    ZenResolveResultType.ZEN_VARIABLE -> {
                        methodType = getVariableType(resolvedMethods[0].element as ZenScriptVariableDeclaration)
                    }

                    ZenResolveResultType.JAVA_PROPERTY -> {
                        methodType = when (val prop = resolvedMethods[0].element) {
                            is PsiField -> ZenType.fromJavaType(prop.type)
                            is PsiMethod -> {
                                val params = prop.parameterList
                                ZenType.fromJavaType(if (params.isEmpty) {
                                    prop.returnType
                                } else {
                                    params.getParameter(0)!!.type
                                })
                            }

                            else -> null
                        }
                    }

                    ZenResolveResultType.JAVA_GLOBAL_VAR -> {
                        when (val el = resolvedMethods[0].element) {
                            is PsiField -> methodType = ZenType.fromJavaType(el.type)
                            is PsiMethod -> {
                                return ZenType.fromJavaType(el.returnType)
                            }
                        }
                    }

                    else -> {
                        return ZenUnknownType("<unknown call>")
                    }
                }
            }
        }

    }

    if (methodType == null) {
        methodType = getType(methodExpr)
    }

    if (methodType is ZenScriptClassType && methodType.isLibrary) {
        val javaClazz = findJavaClass(project, methodType.qualifiedName)
        if (isFunctionalInterface(javaClazz)) {
            methodType = null
        }
    }

    if (methodType is ZenScriptFunctionType) {
        return methodType.returnType
    }

    return ZenUnknownType("${expr.text} is not callable")
}

fun getTypeImpl(expr: ZenScriptLiteralExpression): ZenType {

    return when (expr) {
        is ZenScriptPrimitiveLiteral -> when (expr.firstChild.elementType) {
            ZenScriptTypes.INT_LITERAL -> ZenPrimitiveType.INT
            ZenScriptTypes.LONG_LITERAL -> ZenPrimitiveType.LONG
            ZenScriptTypes.FLOAT_LITERAL -> ZenPrimitiveType.FLOAT
            ZenScriptTypes.DOUBLE_LITERAL -> ZenPrimitiveType.DOUBLE
            ZenScriptTypes.STRING_LITERAL -> ZenPrimitiveType.STRING
            ZenScriptTypes.K_TRUE -> ZenPrimitiveType.BOOL
            ZenScriptTypes.K_FALSE -> ZenPrimitiveType.BOOL
            ZenScriptTypes.K_NULL -> ZenPrimitiveType.NULL
            else -> throw IllegalArgumentException("unknown primitive literal")
        }

        // TODO：better handle this
        is ZenScriptArrayLiteral -> {
            val elements = expr.expressionList
            if (elements.isEmpty()) {
                ZenScriptArrayType(ZenPrimitiveType.ANY)
            } else {
                ZenScriptArrayType(getType(elements[0]))
            }
        }

        is ZenScriptMapLiteral -> {
            val entries = expr.mapEntryList
            if (entries.isEmpty()) {
                ZenScriptMapType(ZenPrimitiveType.ANY, ZenPrimitiveType.ANY)
            } else {
                ZenScriptMapType(getType(entries[0].key), getType(entries[0].value))
            }
        }

        // TODO
        is ZenScriptFunctionLiteral -> ZenPrimitiveType.ANY
        is ZenScriptBracketHandlerLiteral -> ZenPrimitiveType.ANY

        else -> ZenUnknownType(expr.toString())
    }
}

fun getTypeImpl(expr: ZenScriptLocalAccessExpression): ZenType {
    val result = getTargetType(expr.advancedResolve(), false)

    if (result != null) {
        return result
    }

    val id = expr.identifier ?: return ZenUnknownType("<unknown>")
    val text = id.text

    val isPackageName = text == "scripts" || FileBasedIndex.getInstance().getAllKeys(ZenScriptClassNameIndex.NAME, expr.project).any { it -> it.startsWith(text) }

    if (isPackageName) {
        return ZenScriptPackageType(text, text != "scripts")
    }

    return ZenUnknownType(text)
}

fun getTypeImpl(expr: ZenScriptMemberAccessExpression): ZenType {

    val resolveResults = expr.advancedResolve()

    val result = getTargetType(resolveResults, false)

    if (result != null) {
        return result
    }
    val prevType = getType(expr.expression)
    val memberName = expr.identifier?.text ?: return ZenUnknownType("<unknown>")
    val project = expr.project
    return when (prevType) {
        is ZenScriptPackageType -> {
            val packageOrClassName = "${prevType.packageName}.${memberName}"
            var isClass = false
            var found = false
            for (className in FileBasedIndex.getInstance().getAllKeys(ZenScriptClassNameIndex.NAME, project)) {
                if (className == packageOrClassName) {
                    isClass = true
                    found = true
                    break
                }
                if (className.startsWith(packageOrClassName)) {
                    found = true
                    break
                }
            }
            if (!found) {
                ZenUnknownType(packageOrClassName)
            } else if (isClass) {
                ZenScriptClassType(memberName, packageOrClassName)
            } else {
                ZenScriptPackageType(packageOrClassName, prevType.isLibrary)
            }
        }

        ZenPrimitiveType.ANY -> ZenPrimitiveType.ANY
        else -> ZenUnknownType(expr.toString())
    }

}
