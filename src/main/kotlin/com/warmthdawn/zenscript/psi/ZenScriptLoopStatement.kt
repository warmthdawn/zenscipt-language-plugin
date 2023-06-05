package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement

interface ZenScriptLoopStatement : ZenScriptStatement {
    val body: PsiElement?
}