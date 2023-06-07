package com.warmthdawn.zenscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.util.hasStaticModifier

abstract class ZenScriptClassImpl(node: ASTNode) : ZenScriptNamedElementImpl(node), ZenScriptClass {
    override val members: List<PsiElement>
        get() = TODO("Not yet implemented")
    override val methods: List<PsiElement>
        get() = TODO("Not yet implemented")
    override val properties: List<PsiElement>
        get() = TODO("Not yet implemented")
    override val constructors: List<PsiElement>
        get() = TODO("Not yet implemented")


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
        for (member in members) {
            if (member == lastParent) {
                continue
            }
            if (isStatic != member.hasStaticModifier) {
                continue
            }
            if (!processor.execute(member, state)) {
                processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
                return false
            }
        }
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
        return true
    }
}