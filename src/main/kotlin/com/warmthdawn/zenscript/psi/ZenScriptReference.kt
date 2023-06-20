package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.warmthdawn.zenscript.reference.ZenScriptElementResolveResult

interface ZenScriptReference : PsiElement, PsiReference, PsiPolyVariantReference {

    fun advancedResolve(incompleteCode: Boolean = false): Array<ZenScriptElementResolveResult>
}