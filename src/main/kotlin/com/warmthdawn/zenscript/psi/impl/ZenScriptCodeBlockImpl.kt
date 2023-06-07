package com.warmthdawn.zenscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.warmthdawn.zenscript.psi.ZenScriptCodeBlock

abstract class ZenScriptCodeBlockImpl(node: ASTNode) : ASTWrapperPsiElement(node), ZenScriptCodeBlock {

    override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {


        if (lastParent == null || lastParent.parent != this) {
            return true
        }

        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        var prev = lastParent.prevSibling

        while (prev != null) {
            if (!processor.execute(prev, state)) {
                processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
                return false
            }
            prev = lastParent.prevSibling
        }

        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
        return true
    }
}
