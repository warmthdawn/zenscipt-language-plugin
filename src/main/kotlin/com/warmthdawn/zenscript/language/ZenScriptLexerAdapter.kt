package com.warmthdawn.zenscript.language

import com.intellij.lexer.FlexAdapter
import com.warmthdawn.zenscript.grammar.ZenScriptLexer


class ZenScriptLexerAdapter : FlexAdapter(ZenScriptLexer())