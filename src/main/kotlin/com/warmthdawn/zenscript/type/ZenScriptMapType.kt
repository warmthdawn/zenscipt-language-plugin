package com.warmthdawn.zenscript.type

class ZenScriptMapType(
        val keyType: ZenType,
        val valueType: ZenType
): ZenType {
    override val simpleName: String
        get() = "$valueType[$keyType]"
    override val displayName: String
        get() = "$valueType[$keyType]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ZenScriptMapType

        if (keyType != other.keyType) return false
        return valueType == other.valueType
    }

    override fun hashCode(): Int {
        var result = keyType.hashCode()
        result = 31 * result + valueType.hashCode()
        return result
    }


}