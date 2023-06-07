package com.warmthdawn.zenscript.util

import com.intellij.psi.PsiElement
import com.warmthdawn.zenscript.psi.ZenScriptTypes


val PsiElement.hasStaticModifier
    get() = node.findChildByType(ZenScriptTypes.K_STATIC) != null