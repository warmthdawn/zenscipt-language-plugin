package com.warmthdawn.zenscript.type

class ZenScriptMapType(
        val keyType: ZenType,
        val valueType: ZenType
): ZenType {
    override val simpleName: String
        get() = "$valueType[$keyType]"
    override val displayName: String
        get() = "$valueType[$keyType]"
}