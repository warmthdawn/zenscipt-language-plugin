package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement

interface ZenScriptFunction: PsiElement {

    val functionBody: ZenScriptFunctionBody?
    val parameters: ZenScriptParameters?
    val returnType: ZenScriptType?
}