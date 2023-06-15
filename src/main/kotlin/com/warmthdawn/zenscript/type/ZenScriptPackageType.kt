package com.warmthdawn.zenscript.type

data class ZenScriptPackageType(val packageName: String, val isLibrary: Boolean): ZenType {
    override val simpleName: String
        get() = packageName
    override val displayName: String
        get() = packageName
}