package com.warmthdawn.zenscript.psi

import com.intellij.psi.tree.TokenSet

object ZenScriptTokenSet {
    val IDENTIFIERS: TokenSet = TokenSet.create(ZenScriptTypes.ID)
    val COMMENTS: TokenSet = TokenSet.create(ZenScriptTypes.COMMENT)
}
