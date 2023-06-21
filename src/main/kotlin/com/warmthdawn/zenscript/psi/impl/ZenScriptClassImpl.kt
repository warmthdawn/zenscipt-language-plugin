package com.warmthdawn.zenscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.util.hasStaticModifier

abstract class ZenScriptClassImpl(node: ASTNode) : ZenScriptNamedElementImpl(node), ZenScriptClass {
    override val members: List<ZenScriptMember>
        get() = listOf(variables, constructors, functions).flatten()

    override val identifier: ZenScriptIdentifier?
        get() = findChildByClass(ZenScriptQualifiedName::class.java)?.identifier


    override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {

        val isStatic = when (lastParent) {
            is ZenScriptConstructorDeclaration -> false
            is ZenScriptFunctionDeclaration -> lastParent.hasStaticModifier
            is ZenScriptVariableDeclaration -> lastParent.hasStaticModifier
            else -> true
        }

        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        var notFound = true
        for (field in variables) {
            if (isStatic != field.hasStaticModifier) {
                continue
            }
            if (!processor.execute(field, state)) {
                notFound = false
                break
            }
        }
        for (member in functions) {
            if (isStatic != member.hasStaticModifier) {
                continue
            }
            if (!processor.execute(member, state)) {
                notFound = false
            }
        }
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
        return notFound
    }
}