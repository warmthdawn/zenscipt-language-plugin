package com.warmthdawn.zenscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.warmthdawn.zenscript.psi.ZenScriptIdentifier
import com.warmthdawn.zenscript.psi.ZenScriptNamedElement
import com.warmthdawn.zenscript.util.createIdentifierFromText

abstract class ZenScriptNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), ZenScriptNamedElement {

    override val identifier: PsiElement?
        get() = PsiTreeUtil.findChildOfType(this, ZenScriptIdentifier::class.java)!!

    override fun setName(name: String): PsiElement {
        if (identifier == null) return this
        createIdentifierFromText(project, name)?.let {
            node.replaceChild(identifier!!.node, it.node)
        }
        return this
    }

    override fun getName(): String? = identifier?.text


    override fun getNameIdentifier(): PsiElement? = identifier

    override fun getTextOffset(): Int {
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun getPresentation(): ItemPresentation? {
        return super.getPresentation()
    }
}