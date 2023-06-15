package com.warmthdawn.zenscript.type

class ZenScriptArrayType(val elementType: ZenType): ZenType{
    override val simpleName: String
        get() = "${elementType.simpleName}[]"
    override val displayName: String
        get() = "${elementType.displayName}[]"
}