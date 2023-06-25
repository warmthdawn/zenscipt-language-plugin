package com.warmthdawn.zenscript.type

data class ZenScriptPackageType(val packageName: String, val isLibrary: Boolean): ZenType {
    override val simpleName: String
        get() = packageName
    override val displayName: String
        get() = packageName

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ZenScriptPackageType

        return packageName == other.packageName
    }

    override fun hashCode(): Int {
        return packageName.hashCode()
    }


}