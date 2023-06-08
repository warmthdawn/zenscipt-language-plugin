package com.warmthdawn.zenscript.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

abstract class ZenScriptScopeProcessor: PsiScopeProcessor {
    protected var holder: PsiElement? = null
    override fun handleEvent(event: PsiScopeProcessor.Event, associated: Any?) {
        if(event == PsiScopeProcessor.Event.SET_DECLARATION_HOLDER) {
            holder = associated as? PsiElement
        }
    }

}


fun ZenScriptScopeProcessor(executor: (element: PsiElement, parent: PsiElement?, state: ResolveState)->Boolean): ZenScriptScopeProcessor {
    return object: ZenScriptScopeProcessor() {
        override fun execute(element: PsiElement, state: ResolveState): Boolean {
            return executor(element, holder, state)
        }
    }
}