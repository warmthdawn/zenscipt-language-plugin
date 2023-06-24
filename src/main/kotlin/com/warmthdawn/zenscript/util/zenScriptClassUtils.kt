package com.warmthdawn.zenscript.util

import com.intellij.psi.PsiElement
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.type.ZenPrimitiveType
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

val ZenScriptParameter.type: ZenType
    get() {
        val typeRef = this.typeRef
        return if (typeRef != null) {
            ZenType.fromTypeRef(typeRef)
        } else {
            val ownerFunction = this.parent.parent
            var result: ZenType = ZenPrimitiveType.ANY
            if (ownerFunction is ZenScriptFunctionLiteral) {
                // TODO predict
            } else if (this.initializer != null) {
                result = getType(this.initializer!!.expression) // TODO: actual type is any
            }
            result
        }
    }
val ZenScriptForeachVariableDeclaration.type: ZenType
    get() {
        var result: ZenType = ZenPrimitiveType.ANY

        val forEachStmt = this.parent as ZenScriptForeachStatement

        val targetType = getType(forEachStmt.iterTarget)

        // TODO foreach

        return result
    }

val ZenScriptFunction.returnType: ZenType
    get() {
        val typeRef = this.returnTypeRef
        return if (typeRef != null) {
            ZenType.fromTypeRef(typeRef)
        } else {
            val ownerFunction = this.parent.parent
            var result: ZenType = ZenPrimitiveType.ANY
            // TODO predict
            if (ownerFunction is ZenScriptFunctionLiteral) {
                // TODO predict
            }
            result
        }
    }

