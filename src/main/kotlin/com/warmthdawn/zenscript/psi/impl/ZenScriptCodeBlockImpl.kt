package com.warmthdawn.zenscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.warmthdawn.zenscript.psi.ZenScriptCodeBlock

abstract class ZenScriptCodeBlockImpl(node: ASTNode) : ASTWrapperPsiElement(node), ZenScriptCodeBlock {
}
