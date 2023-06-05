package com.warmthdawn.zenscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.warmthdawn.zenscript.psi.ZenScriptClass
import com.warmthdawn.zenscript.psi.ZenScriptClassType
import com.warmthdawn.zenscript.psi.ZenScriptIdentifier
import com.warmthdawn.zenscript.psi.ZenScriptQualifiedName

abstract class ZenScriptClassImpl(node: ASTNode) : ZenScriptNamedElementImpl(node), ZenScriptClass {
    override val parents: List<ZenScriptClassType>
        get() = TODO("Not yet implemented")
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
}