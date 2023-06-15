package com.warmthdawn.zenscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.elementType
import com.warmthdawn.zenscript.psi.ZenScriptCodeBlock
import com.warmthdawn.zenscript.psi.ZenScriptTokenSet

abstract class ZenScriptCodeBlockImpl(node: ASTNode) : ASTWrapperPsiElement(node), ZenScriptCodeBlock {

    override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {


        if (lastParent == null || lastParent.parent != this) {
            return true
        }

        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        var prev = lastParent

        while (prev != null) {
            if (ZenScriptTokenSet.HIDDEN_TOKENS.contains(prev.elementType)) {
                continue
            }
            if (!processor.execute(prev, state)) {
                processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
                return false
            }
            prev = prev.prevSibling
        }

        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
        return true
    }
}
