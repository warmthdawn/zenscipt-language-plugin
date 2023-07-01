package com.warmthdawn.zenscript.annotator

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.warmthdawn.zenscript.bundle.ZenScriptBundle
import com.warmthdawn.zenscript.psi.ZenScriptAssignmentExpression
import com.warmthdawn.zenscript.psi.ZenScriptExpression
import com.warmthdawn.zenscript.type.ZenScriptTypeService
import com.warmthdawn.zenscript.type.ZenType
import com.warmthdawn.zenscript.type.assignmentAcceptedType
import com.warmthdawn.zenscript.type.getType

class ZenScriptTypeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is ZenScriptAssignmentExpression) {
            checkAssignment(element, holder)
        }
    }


    private fun checkAssignment(element: ZenScriptAssignmentExpression, holder: AnnotationHolder) {
        val assignee = element.left
        val expr = element.right ?: return

        val acceptedType = assignmentAcceptedType(assignee)
        if (acceptedType == null) {
            generateNotAssignable(assignee, holder)
            return
        }
        val exprType = getType(expr)

        if (!ZenScriptTypeService.getInstance(element.project).canCast(acceptedType, exprType)) {
            generateTypeMismatch(acceptedType, exprType, expr, holder)
        }

    }


    private fun generateTypeMismatch(acceptedType: ZenType, actualType: ZenType, expression: ZenScriptExpression, holder: AnnotationHolder) {
        val range = expression.textRange

        val message = ZenScriptBundle.message("error.type.mismatch", acceptedType.displayName, actualType.displayName)

        val builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
                .range(range)
                .highlightType(ProblemHighlightType.GENERIC_ERROR)

        builder.create()

    }

    private fun generateNotAssignable(assignLeftExpr: ZenScriptExpression, holder: AnnotationHolder) {
        val range = assignLeftExpr.textRange

        val message = ZenScriptBundle.message("error.type.not.assignable")

        val builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
                .range(range)
                .highlightType(ProblemHighlightType.GENERIC_ERROR)

        builder.create()
    }

}