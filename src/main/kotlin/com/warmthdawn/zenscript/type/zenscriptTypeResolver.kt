package com.warmthdawn.zenscript.type

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.elementType
import com.intellij.util.indexing.FileBasedIndex
import com.jetbrains.rd.util.qualifiedName
import com.warmthdawn.zenscript.index.ZenScriptClassNameIndex
import com.warmthdawn.zenscript.index.ZenScriptMemberCache
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.util.type


fun getType(expr: ZenScriptExpression?): ZenType {
    return when (expr) {
        is ZenScriptLocalAccessExpression -> getTypeImpl(expr)
        is ZenScriptMemberAccessExpression -> getTypeImpl(expr)
        is ZenScriptCallExpression -> getTypeImpl(expr)
        is ZenScriptArrayIndexExpression -> getTypeImpl(expr)
        is ZenScriptInstanceOfExpression -> ZenScriptPrimitiveType.BOOL
        is ZenScriptTypeCastExpression -> ZenType.fromTypeRef(expr.typeRef)
        is ZenScriptLiteralExpression -> getTypeImpl(expr)
        is ZenScriptParenExpression -> getType(expr.expression)
        // ops

        is ZenScriptBinaryExpression -> getTypeImpl(expr)
        is ZenScriptUnaryExpression -> getTypeImpl(expr)
        is ZenScriptConditionalExpression -> getTypeImpl(expr)

        null -> ZenUnknownType("<unknown>")
        else -> ZenUnknownType(expr.toString())
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
    TODO()
}

fun getTypeImpl(expr: ZenScriptCallExpression): ZenType {
    TODO()
}

fun getTypeImpl(expr: ZenScriptLiteralExpression): ZenType {

    return when (expr) {
        is ZenScriptPrimitiveLiteral -> when (expr.firstChild.elementType) {
            ZenScriptTypes.INT_LITERAL -> ZenScriptPrimitiveType.INT
            ZenScriptTypes.LONG_LITERAL -> ZenScriptPrimitiveType.LONG
            ZenScriptTypes.FLOAT_LITERAL -> ZenScriptPrimitiveType.FLOAT
            ZenScriptTypes.DOUBLE_LITERAL -> ZenScriptPrimitiveType.DOUBLE
            ZenScriptTypes.STRING_LITERAL -> ZenScriptPrimitiveType.STRING
            ZenScriptTypes.K_TRUE -> ZenScriptPrimitiveType.BOOL
            ZenScriptTypes.K_FALSE -> ZenScriptPrimitiveType.BOOL
            ZenScriptTypes.K_NULL -> ZenScriptPrimitiveType.NULL
            else -> throw IllegalArgumentException("unknown primitive literal")
        }

        // TODO：better handle this
        is ZenScriptArrayLiteral -> {
            val elements = expr.expressionList
            if (elements.isEmpty()) {
                ZenScriptArrayType(ZenScriptPrimitiveType.ANY)
            } else {
                ZenScriptArrayType(getType(elements[0]))
            }
        }

        is ZenScriptMapLiteral -> {
            val entries = expr.mapEntryList
            if (entries.isEmpty()) {
                ZenScriptMapType(ZenScriptPrimitiveType.ANY, ZenScriptPrimitiveType.ANY)
            } else {
                ZenScriptMapType(getType(entries[0].key), getType(entries[0].value))
            }
        }

        // TODO
        is ZenScriptFunctionLiteral -> ZenScriptPrimitiveType.ANY
        is ZenScriptBracketHandlerLiteral -> ZenScriptPrimitiveType.ANY

        else -> ZenUnknownType(expr.toString())
    }
}

fun getTypeImpl(expr: ZenScriptLocalAccessExpression): ZenType {
    val element = expr.resolve()
    if(element != null) {
        return when(element) {
            is ZenScriptVariableDeclaration -> element.type
            is ZenScriptClassDeclaration -> {
                val file = element.containingFile as ZenScriptFile
                ZenScriptClassType(file.packageName + "." + element.qualifiedName!!.text)
            }
            is ZenScriptFunctionDeclaration -> ZenScriptPrimitiveType.ANY
            is ZenScriptImportDeclaration -> element.resolve()?.let {
                val qualifiedName = element.qualifiedName!!.text
                when(it) {
                    is PsiClass, is ZenScriptClassDeclaration -> ZenScriptClassType(qualifiedName)
                    is PsiMethod -> ZenScriptPrimitiveType.ANY // TODO: Method
                    is PsiField -> ZenType.fromJavaType(it.type)
                    else -> throw IllegalArgumentException()
                }
            } ?: return ZenUnknownType(element.qualifiedName!!.text)

            else -> ZenUnknownType(element.text)
        }
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

        is ZenScriptClassType -> {
            if (prevType.isLibrary) {
                val javaClazz = findJavaClass(project, prevType.qualifiedName)
                        ?: return ZenUnknownType(prevType.qualifiedName)
                val members = ZenScriptMemberCache.getInstance(project).getMembers(javaClazz)
                members.properties[memberName]?.zsType ?: ZenUnknownType(prevType.qualifiedName + "." + memberName)
            } else {
                val zenClazz = findZenClass(project, prevType.qualifiedName)
                        ?: return ZenUnknownType(prevType.qualifiedName)
                val field = zenClazz.variableDeclarationList.first {
                    it.name == memberName
                }
                ZenType.fromTypeRef(field.typeRef)
            }
        }


        else -> ZenUnknownType(expr.toString())
    }
}
