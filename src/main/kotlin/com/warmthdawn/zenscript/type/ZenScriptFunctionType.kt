package com.warmthdawn.zenscript.type

class ZenScriptFunctionType(val parameterTypes: List<ZenType>, val returnType: ZenType) : ZenType {

    override val simpleName: String
        get() = "function(${parameterTypes.joinToString(",") { it.simpleName }})${returnType.simpleName}"
    override val displayName: String
        get() = "function(${parameterTypes.joinToString(",") { it.displayName }})${returnType.displayName}"
}