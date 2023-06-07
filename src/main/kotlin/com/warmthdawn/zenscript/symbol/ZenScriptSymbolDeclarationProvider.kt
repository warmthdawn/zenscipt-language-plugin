package com.warmthdawn.zenscript.symbol

import com.intellij.model.psi.PsiSymbolDeclaration
import com.intellij.model.psi.PsiSymbolDeclarationProvider
import com.intellij.psi.PsiElement

class ZenScriptSymbolDeclarationProvider: PsiSymbolDeclarationProvider {
    override fun getDeclarations(
        element: PsiElement,
        offsetInElement: Int
    ): MutableCollection<out PsiSymbolDeclaration> {
        TODO("Not yet implemented")
    }
}