package com.warmthdawn.zenscript.util

import com.intellij.psi.PsiElement
import com.warmthdawn.zenscript.psi.ZenScriptTypes
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.type.ZenType
import com.warmthdawn.zenscript.type.getType


val PsiElement.hasStaticModifier
    get() = node.findChildByType(ZenScriptTypes.K_STATIC) != null


val ZenScriptVariableDeclaration.hasGlobalModifier
    get() = node.findChildByType(ZenScriptTypes.K_GLOBAL) != null
val ZenScriptVariableDeclaration.isReadonly
    get() = node.findChildByType(ZenScriptTypes.K_VAR) == null


val ZenScriptVariableDeclaration.type: ZenType
    get() {
        val typeRef = this.typeRef
        return if (typeRef != null) {
            ZenType.fromTypeRef(typeRef)
        } else {
            getType(this.initializer?.expression)
        }
    }