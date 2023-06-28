package com.warmthdawn.zenscript.type

import com.intellij.openapi.diagnostic.Logger


private val logger = Logger.getInstance(ZenUnknownType::class.java)
class ZenUnknownType(text: String) : ZenType {
    init {
        logger.warn("Unknown type found: $text")
    }
    override val simpleName: String = "Unknown: $text"
    override val displayName: String
        get() = "[$simpleName]"
}