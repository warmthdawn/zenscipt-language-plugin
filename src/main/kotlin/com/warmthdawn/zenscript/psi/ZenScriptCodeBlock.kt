package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement

interface ZenScriptCodeBlock : PsiElement {
    val statements: List<ZenScriptStatement>
}