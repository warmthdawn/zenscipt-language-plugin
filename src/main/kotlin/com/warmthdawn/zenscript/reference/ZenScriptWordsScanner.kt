package com.warmthdawn.zenscript.reference

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.psi.tree.TokenSet
import com.warmthdawn.zenscript.language.ZenScriptLexerAdapter
import com.warmthdawn.zenscript.psi.ZenScriptTokenSet
import com.warmthdawn.zenscript.psi.ZenScriptTypes

class ZenScriptWordsScanner : DefaultWordsScanner(
    ZenScriptLexerAdapter(),
    TokenSet.create(ZenScriptTypes.IDENTIFIER).also { ZenScriptTokenSet.KEYWORDS },
    ZenScriptTokenSet.COMMENTS,
    TokenSet.create(
        ZenScriptTypes.INT_LITERAL,
        ZenScriptTypes.FLOAT_LITERAL,
        ZenScriptTypes.STRING_LITERAL,
        ZenScriptTypes.K_NULL,
        ZenScriptTypes.K_TRUE,
        ZenScriptTypes.K_FALSE,
    )
)