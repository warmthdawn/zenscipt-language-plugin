package com.warmthdawn.zenscript.util

import com.intellij.psi.PsiElement
import com.warmthdawn.zenscript.reference.ZenResolveResultType
import com.warmthdawn.zenscript.reference.ZenScriptElementResolveResult


fun Array<ZenScriptElementResolveResult>.fetchMethods(): List<PsiElement> {
    return this.asSequence()
        .filter { it.type == ZenResolveResultType.ZEN_METHOD || it.type == ZenResolveResultType.JAVA_METHODS || it.type == ZenResolveResultType.JAVA_GLOBAL_FUNCTION  }
        .map { it.element }
        .toList()
}

