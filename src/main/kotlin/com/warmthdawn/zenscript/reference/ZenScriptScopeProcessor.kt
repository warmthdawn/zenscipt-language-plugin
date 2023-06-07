package com.warmthdawn.zenscript.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

class ZenScriptScopeProcessor: PsiScopeProcessor {
    private var holder: PsiElement? = null
    override fun handleEvent(event: PsiScopeProcessor.Event, associated: Any?) {
        if(event == PsiScopeProcessor.Event.SET_DECLARATION_HOLDER) {
            holder = associated as? PsiElement
        }
    }

    override fun execute(element: PsiElement, state: ResolveState): Boolean {

        return true
    }
}