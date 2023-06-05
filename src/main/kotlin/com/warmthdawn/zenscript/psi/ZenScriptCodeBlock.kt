package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifiableCodeBlock

interface ZenScriptCodeBlock : PsiElement, PsiModifiableCodeBlock {
    val statements: List<ZenScriptStatement>
}