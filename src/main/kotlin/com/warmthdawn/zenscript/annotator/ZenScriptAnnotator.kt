package com.warmthdawn.zenscript.annotator

import com.intellij.analysis.AnalysisBundle
import com.intellij.codeInspection.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.warmthdawn.zenscript.psi.ZenScriptLocalAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptMemberAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptReference
import com.warmthdawn.zenscript.type.ZenPrimitiveType
import com.warmthdawn.zenscript.type.ZenScriptPackageType
import com.warmthdawn.zenscript.type.getType

class ZenScriptAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        if (element is ZenScriptReference) {
            if (!checkRef(element)) {
                generateError(element, holder)
            }
        }
    }


    private fun checkRef(ref: ZenScriptReference): Boolean {
        if (ref is ZenScriptMemberAccessExpression) {
            val qualifierType = getType(ref.expression)
            if (qualifierType == ZenPrimitiveType.ANY) {
                return true
            }
            if (qualifierType is ZenScriptPackageType) {
                return true
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

    private fun generateError(ref: ZenScriptReference, holder: AnnotationHolder) {
        val rangeInElement = ref.rangeInElement

        val range = TextRange.from(ref.element.textRange.startOffset
                + rangeInElement.startOffset, rangeInElement.length)

        val message = AnalysisBundle.message("cannot.resolve.symbol", ref.canonicalText)

        var builder = holder.newAnnotation(HighlightSeverity.ERROR, message)
                .range(range)
                .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)

        if (ref is LocalQuickFixProvider) {
            val fixes = ref.quickFixes
            if (fixes != null) {
                val inspectionManager = InspectionManager.getInstance(ref.element.project)
                for (fix in fixes) {
                    val descriptor = inspectionManager.createProblemDescriptor(ref.element, message, fix,
                            ProblemHighlightType.LIKE_UNKNOWN_SYMBOL, true)
                    builder = builder.newLocalQuickFix(fix, descriptor).registerFix()
                }
            }
        }

        builder.create()
    }

}