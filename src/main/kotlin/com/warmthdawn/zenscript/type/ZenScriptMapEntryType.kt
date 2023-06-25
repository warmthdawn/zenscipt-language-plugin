package com.warmthdawn.zenscript.type

class ZenScriptMapEntryType(
        val keyType: ZenType,
        val valueType: ZenType
): ZenType {
    override val simpleName: String
        get() = "MapEntry_from_${keyType}_to_$valueType"
    override val displayName: String
        get() = "Map.Entry<$keyType, $valueType>"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ZenScriptMapEntryType

        if (keyType != other.keyType) return false
        return valueType == other.valueType
    }

    override fun hashCode(): Int {
        var result = keyType.hashCode()
        result = 31 * result + valueType.hashCode()
        return result
    }


}