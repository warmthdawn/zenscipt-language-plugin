package com.warmthdawn.zenscript.type

class ZenScriptClassType(override val name: String, val qualifiedName: String) : ZenType {
    override val displayName: String
        get() = qualifiedName
}