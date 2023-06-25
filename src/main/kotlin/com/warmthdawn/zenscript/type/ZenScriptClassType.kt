package com.warmthdawn.zenscript.type

class ZenScriptClassType(override val simpleName: String, val qualifiedName: String) : ZenType {
    constructor(qualifiedName: String) : this(qualifiedName.substringAfterLast('.'), qualifiedName) {

    }
    override val displayName: String
        get() = simpleName

    val isLibrary = !qualifiedName.startsWith("scripts")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ZenScriptClassType

        return qualifiedName == other.qualifiedName
    }

    override fun hashCode(): Int {
        return qualifiedName.hashCode()
    }


}