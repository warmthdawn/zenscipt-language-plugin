package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

@JvmDefaultWithCompatibility
interface ZenScriptFunction: PsiElement {

    val functionBody: ZenScriptFunctionBody?
    val parameters: ZenScriptParameters?
    val returnTypeRef: ZenScriptTypeRef?


    override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {

        if (lastParent != this.functionBody) {
            return true
        }
        val params = this.parameters ?: return true
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        for (parameter in params.parameterList) {
            if (!processor.execute(parameter, state)) {
                processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
                return false
            }
        }
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
        return true
    }
}