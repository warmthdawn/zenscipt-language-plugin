package com.warmthdawn.zenscript.type

class ZenUnknownType(text: String) : ZenType {
    override val simpleName: String = "Unknown: $text"
    override val displayName: String
        get() = simpleName
}