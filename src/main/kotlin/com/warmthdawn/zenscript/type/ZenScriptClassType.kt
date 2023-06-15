package com.warmthdawn.zenscript.type

class ZenScriptClassType(override val simpleName: String, val qualifiedName: String) : ZenType {
    constructor(qualifiedName: String) : this(qualifiedName.substringAfterLast('.'), qualifiedName) {

    }
    override val displayName: String
        get() = qualifiedName

    val isLibrary = !qualifiedName.startsWith("scripts")
}