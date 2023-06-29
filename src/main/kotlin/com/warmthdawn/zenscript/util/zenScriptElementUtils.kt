package com.warmthdawn.zenscript.util

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.type.*


val PsiElement.hasStaticModifier
    get() = node.findChildByType(ZenScriptTypes.K_STATIC) != null


val ZenScriptVariableDeclaration.hasGlobalModifier
    get() = node.findChildByType(ZenScriptTypes.K_GLOBAL) != null
val ZenScriptVariableDeclaration.isReadonly
    get() = node.findChildByType(ZenScriptTypes.K_VAR) == null


val ZenScriptVariableDeclaration.type: ZenType
    get() {
        val typeRef = this.typeRef
        val initializer = this.initializer?.expression

        return if (typeRef == null && initializer == null) {
            ZenPrimitiveType.ANY
        } else if (typeRef != null) {
            ZenType.fromTypeRef(typeRef)
        } else {
            getType(initializer)
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
val ZenScriptArguments.argumentTypes: List<ZenType>
    get() {
        return expressionList.map { getType(it) }
    }


data class MethodMetadata(
    val parameterTypes: List<ZenType>,
    val parameterNames: List<String>?,
    val firstOptionalIndex: Int = -1,
    val isVarargs: Boolean = false
)

fun PsiMethod.extractMethodMetadata(isExpand: Boolean = false): MethodMetadata {

    val parameters = this.parameterList.parameters

    var firstOptionalIndex = -1
    val isVarargs = this.isVarArgs
    val parameterTypes = ArrayList<ZenType>(parameters.size)
    val parameterNames = ArrayList<String>(parameters.size)
    for (i in parameters.indices) {
        val paramType = ZenType.fromJavaType(parameters[i].type)
        parameterTypes.add(paramType)
        parameterNames.add(parameters[i].name)
        if (parameters[i].hasAnnotation("stanhebben.zenscript.annotations.Optional")) {
            firstOptionalIndex = i
            break
        }
    }
    return MethodMetadata(parameterTypes, parameterNames, firstOptionalIndex, isVarargs)
}

fun ZenScriptFunction.extractMethodMetadata(): MethodMetadata {
    var firstOptionalIndex = -1
    val parameters = this.parameters!!.parameterList
    val parameterTypes = ArrayList<ZenType>(parameters.size)
    val parameterNames = ArrayList<String>(parameters.size)
    for (i in parameters.indices) {
        val paramType = ZenType.fromTypeRef(parameters[i].typeRef)
        parameterTypes.add(paramType)
        parameterNames.add(parameters[i].name!!)
        if (parameters[i].initializer != null) {
            firstOptionalIndex = i
            break
        }
    }
    return MethodMetadata(parameterTypes, parameterNames, firstOptionalIndex, false)
}

fun ZenScriptFunctionType.extractMethodMetadata(): MethodMetadata {
    val firstOptionalIndex = if (this is ZenScriptFunctionTypeNamedParameters) this.firstOptionalIndex else -1
    val isVarargs = if (this is ZenScriptFunctionTypeNamedParameters) this.isVarargs else false
    val parameterTypes = this.parameterTypes
    val parameterNames = if (this is ZenScriptFunctionTypeNamedParameters) this.parameterNames else null
    return MethodMetadata(parameterTypes, parameterNames, firstOptionalIndex, isVarargs)
}