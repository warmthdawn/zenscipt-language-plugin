package com.warmthdawn.zenscript.misc

import com.intellij.codeInsight.editorActions.JavaLikeQuoteHandler
import com.intellij.codeInsight.editorActions.QuoteHandler
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.warmthdawn.zenscript.psi.ZenScriptTypes

class ZenScriptQuoteHandler : SimpleTokenSetQuoteHandler(ZenScriptTypes.STRING_LITERAL)