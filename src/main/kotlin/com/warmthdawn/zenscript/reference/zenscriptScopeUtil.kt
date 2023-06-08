package com.warmthdawn.zenscript.reference

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiInvalidElementAccessException
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor


private val log = Logger.getInstance("zenscript-scope-util")
fun treeWalkUp(processor: PsiScopeProcessor,
               entrance: PsiElement,
               maxScope: PsiElement?,
               state: ResolveState): Boolean {
    if (!entrance.isValid) {
        log.error(PsiInvalidElementAccessException(entrance))
    }
    var prevParent: PsiElement? = entrance
    var scope: PsiElement? = entrance
    while (scope != null) {
        ProgressIndicatorProvider.checkCanceled()
        if (!scope.processDeclarations(processor, state, prevParent, entrance)) {
            return false // resolved
        }
        if (scope === maxScope) break
        prevParent = scope
        scope = scope.context
    }
    return true
}
