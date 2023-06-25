package com.warmthdawn.zenscript.type

class ZenScriptArrayType(val elementType: ZenType): ZenType{
    override val simpleName: String
        get() = "${elementType.simpleName}[]"
    override val displayName: String
        get() = "${elementType.displayName}[]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ZenScriptArrayType

        return elementType == other.elementType
    }

    override fun hashCode(): Int {
        return elementType.hashCode()
    }


}