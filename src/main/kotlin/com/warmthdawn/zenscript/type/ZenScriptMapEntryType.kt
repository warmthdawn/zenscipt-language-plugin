package com.warmthdawn.zenscript.type

class ZenScriptMapEntryType(
        val keyType: ZenType,
        val valueType: ZenType
): ZenType {
    override val simpleName: String
        get() = "MapEntry_from_${keyType}_to_$valueType"
    override val displayName: String
        get() = "Map.Entry<$keyType, $valueType>"
}