package com.warmthdawn.zenscript.language

import com.intellij.lexer.FlexAdapter
import com.warmthdawn.zenscript.lexer.ZenScriptLexer


class ZenScriptLexerAdapter : FlexAdapter(ZenScriptLexer())