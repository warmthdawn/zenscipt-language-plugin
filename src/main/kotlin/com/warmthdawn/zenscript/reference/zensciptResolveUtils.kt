package com.warmthdawn.zenscript.reference

import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.warmthdawn.zenscript.psi.ZenScriptLocalAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptNamedElement
import com.warmthdawn.zenscript.psi.ZenScriptReference

fun resolveZenScriptReference(ref: ZenScriptReference, incompleteCode: Boolean): Array<ResolveResult> {

    return when (ref) {
        is ZenScriptLocalAccessExpression -> resolveLocalAccessExpr(ref)
        else -> emptyArray()
    }
}


private fun resolveLocalAccessExpr(ref: ZenScriptLocalAccessExpression): Array<ResolveResult> {
    val identifier = ref.identifier

    if (identifier != null) {
        val name = identifier.text
        var found: ZenScriptNamedElement? = null
        val processor = ZenScriptScopeProcessor { element, parent, _ ->
            if (element is ZenScriptNamedElement && element.name == name) {
                found = element
                false
            } else {
                true
            }
        }
        val notFound = PsiTreeUtil.treeWalkUp(processor, ref, null, ResolveState.initial())
        return if (notFound) {
            // todo packages
            emptyArray()
        } else {
            arrayOf(PsiElementResolveResult(found!!.identifier!!))
        }
    }

    return emptyArray()

}

