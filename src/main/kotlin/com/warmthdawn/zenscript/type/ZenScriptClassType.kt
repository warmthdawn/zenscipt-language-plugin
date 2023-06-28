package com.warmthdawn.zenscript.type

open class ZenScriptClassType(
    override val simpleName: String,
    val qualifiedName: String,
    val isAnonymous: Boolean = false
) : ZenType {
    constructor(qualifiedName: String, isAnonymous: Boolean = false) : this(
        qualifiedName.substringAfterLast('.'),
        qualifiedName,
        isAnonymous
    ) {

    }

    override val displayName: String
        get() {
            return if (isAnonymous) {
                "[$simpleName]"
            } else {
                simpleName
            }
        }

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