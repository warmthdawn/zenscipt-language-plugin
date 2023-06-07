package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

interface ZenScriptClass: PsiElement, ZenScriptNamedElement {

    val members: List<PsiElement>

    val methods: List<PsiElement>

    val properties: List<PsiElement>

    val constructors: List<PsiElement>
}