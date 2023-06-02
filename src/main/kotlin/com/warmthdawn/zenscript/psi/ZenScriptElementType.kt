package com.warmthdawn.zenscript.psi

import com.intellij.psi.tree.IElementType
import com.warmthdawn.zenscript.ZSLanguage

class ZenScriptElementType(debugName: String): IElementType(debugName, ZSLanguage) {
    override fun toString(): String {
        return "ZenScriptTokenType." + super.toString()
    }
}