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
        val dartReferences = PsiTreeUtil.getChildrenOfType(this, ZenScriptReference::class.java)
        if (!dartReferences.isNullOrEmpty()) {
            val lastReferenceRange: TextRange = dartReferences[dartReferences.size - 1].textRange
            return UnfairTextRange(
                lastReferenceRange.startOffset - textRange.startOffset,
                lastReferenceRange.endOffset - textRange.endOffset
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

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        TODO()
//        ResolveCache.getInstance(project).resolveWithCaching(
//        ResolveCache.getInstance(project).resolveWithCaching(this, ZenScriptReferenceResolver, true, incompleteCode);
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