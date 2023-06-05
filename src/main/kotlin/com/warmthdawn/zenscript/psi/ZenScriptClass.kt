package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement

interface ZenScriptClass: PsiElement, ZenScriptNamedElement {

    val parents: List<ZenScriptClassType>

    val members: List<PsiElement>

    val methods: List<PsiElement>

    val properties: List<PsiElement>

    val constructors: List<PsiElement>


}