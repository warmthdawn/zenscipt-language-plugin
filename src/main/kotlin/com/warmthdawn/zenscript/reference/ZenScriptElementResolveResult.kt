package com.warmthdawn.zenscript.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveResult

class ZenScriptElementResolveResult(
        private val element: PsiElement,
        val type: ZenResolveResultType,
        private val validResult: Boolean = true,
): ResolveResult {
    override fun getElement(): PsiElement = this.element

    override fun isValidResult(): Boolean = this.validResult
}

enum class ZenResolveResultType {
    ZEN_CLASS,
    ZEN_METHOD,
    ZEN_VARIABLE,
    ZEN_IMPORT,
    ZEN_FILE,

    JAVA_CLASS,
    JAVA_PROPERTY,
    JAVA_METHODS,
    JAVA_OPERATOR,
    JAVA_GLOBAL_VAR,

}