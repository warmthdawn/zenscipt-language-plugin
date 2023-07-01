package com.warmthdawn.zenscript.type

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.warmthdawn.zenscript.psi.ZenScriptExpression
import com.warmthdawn.zenscript.psi.ZenScriptForeachVariableDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptLocalAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptMemberAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptParameter
import com.warmthdawn.zenscript.psi.ZenScriptReference
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.util.isReadonly
import com.warmthdawn.zenscript.util.type

private val logger = Logger.getInstance("zenscript-type-utils")
fun assignmentAcceptedType(expr: ZenScriptExpression): ZenType? {
    return when (expr) {
        is ZenScriptLocalAccessExpression, is ZenScriptMemberAccessExpression -> {
            val reference = expr as ZenScriptReference
            val resolveResults = reference.advancedResolve()
            if (resolveResults.isEmpty()) {
                return null
            }
            val first = resolveResults[0].element
            if (isReadOnly(first)) {
                return null
            }
            return variableType(first)
        }

        else -> {
            logger.warn("unknown element for assignment: $expr")
            null
        }
    }
}

private fun variableType(element: PsiElement): ZenType? {
    return when (element) {
        is ZenScriptVariableDeclaration -> element.type
        is PsiField -> ZenType.fromJavaType(element.type)
        is PsiMethod -> {
            val parameters = element.parameterList.parameters
            if (parameters.isNotEmpty()) {
                ZenType.fromJavaType(parameters[0].type)
            } else {
                null
            }
        }

        else -> {
            logger.warn("unknown element resolved for assignment: $element")
            null
        }
    }
}

private fun isReadOnly(element: PsiElement): Boolean {
    return when (element) {
        is ZenScriptVariableDeclaration -> element.isReadonly
        is ZenScriptParameter, is ZenScriptForeachVariableDeclaration -> true
        is PsiField -> element.hasModifierProperty(PsiModifier.FINAL)
        is PsiMethod -> !element.hasAnnotation("stanhebben.zenscript.annotations.ZenSetter")
        else -> {
            logger.warn("unknown element resolved for assignment: $element")
            true
        }
    }
}