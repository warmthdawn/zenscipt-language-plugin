package com.warmthdawn.zenscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.UnfairTextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import com.warmthdawn.zenscript.psi.ZenScriptNamedElement
import com.warmthdawn.zenscript.psi.ZenScriptReference
import com.warmthdawn.zenscript.reference.ZenScriptElementResolveResult
import com.warmthdawn.zenscript.reference.resolveZenScriptReference


open class ZenScriptReferenceImpl(node: ASTNode) : ASTWrapperPsiElement(node), ZenScriptReference,
        PsiPolyVariantReference {

    override fun getElement(): PsiElement {
        return this
    }

    override fun getReference(): PsiReference? {
        return this
    }

    override fun getRangeInElement(): TextRange {
        val textRange = textRange
        val zsRef = PsiTreeUtil.getChildrenOfType(this, ZenScriptReference::class.java)
        if (!zsRef.isNullOrEmpty()) {
            val lastReferenceRange: TextRange = zsRef[zsRef.size - 1].rangeInElement
            val offset: Int = zsRef[zsRef.size - 1].startOffsetInParent
            return UnfairTextRange(
                    lastReferenceRange.startOffset + offset,
                    lastReferenceRange.endOffset + offset
            )
        }
        return UnfairTextRange(0, textRange.endOffset - textRange.startOffset)
    }

    override fun getCanonicalText(): String {
        return text
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        if (this is ZenScriptNamedElement) {
            this.setName(newElementName)
        }
        throw IncorrectOperationException("Cannot rename")
    }

    override fun bindToElement(element: PsiElement): PsiElement {
        return this
    }


    override fun isSoft(): Boolean {
        return false
    }

    override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {
        return advancedResolve(incompleteCode)
    }

    override fun advancedResolve(incompleteCode: Boolean): Array<ZenScriptElementResolveResult> {
        return ResolveCache.getInstance(project).resolveWithCaching(this, ResolveCache.AbstractResolver { ref, inc ->
            resolveZenScriptReference(ref, inc)
        }, true, incompleteCode) ?: emptyArray()
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(true)
        return if (resolveResults.size == 1 && resolveResults[0].isValidResult) resolveResults[0].element else null
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        val results = multiResolve(false)
        for (result in results) {
            if (getElement().manager.areElementsEquivalent(result.element, element)) {
                return true
            }
        }
        return false
    }
}