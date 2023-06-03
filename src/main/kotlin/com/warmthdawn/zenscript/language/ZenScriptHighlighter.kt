package com.warmthdawn.zenscript.language

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.warmthdawn.zenscript.psi.ZenScriptTokenSet
import com.warmthdawn.zenscript.psi.ZenScriptTypes
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private fun attr(name: String, fallback: TextAttributesKey): TextAttributesKey {
    return createTextAttributesKey("ZENSCRIPT.$name", fallback)
}


object ZenScriptHighlightColors {
    val BLOCK_COMMENT = attr("BLOCK_COMMENT", Default.BLOCK_COMMENT)
    val LINE_COMMENT = attr("LINE_COMMENT", Default.LINE_COMMENT)
    val DOC_COMMENT = attr("DOC_COMMENT", Default.DOC_COMMENT)
    val PREPROCESSOR = attr("PREPROCESSOR", Default.LINE_COMMENT)


    val KEYWORD = attr("KEYWORD", Default.KEYWORD)
    val IDENTIFIER = attr("IDENTIFIER", Default.IDENTIFIER)
    val NUMBER = attr("NUMBER", Default.NUMBER)
    val STRING = attr("STRING", Default.STRING)

    val OPERATOR = attr("OPERATOR", Default.OPERATION_SIGN)
    val BRACKETS = attr("BRACKETS", Default.BRACKETS)
    val BRACES = attr("BRACES", Default.BRACES)
    val DOT = attr("DOT", Default.DOT)
    val COMMA = attr("COMMA", Default.COMMA)
    val SEMICOLON = attr("SEMICOLON", Default.SEMICOLON)
    val PARENTHESES = attr("PARENTHESES", Default.PARENTHESES)

    val BRACKET_HANDLER = attr("BRACKET_HANDLER", Default.HIGHLIGHTED_REFERENCE)
}

class ZenScriptHighlighter : SyntaxHighlighterBase() {

    private val attr1 = mutableMapOf<IElementType, TextAttributesKey>()
    private val attr2 = mutableMapOf<IElementType, TextAttributesKey>()

    init {
        fillMap(attr1, ZenScriptTokenSet.KEYWORDS, ZenScriptHighlightColors.KEYWORD)
        fillMap(attr1, ZenScriptTokenSet.OPERATORS, ZenScriptHighlightColors.OPERATOR)

        // literal
        attr1[ZenScriptTypes.INT_LITERAL] = ZenScriptHighlightColors.NUMBER
        attr1[ZenScriptTypes.LONG_LITERAL] = ZenScriptHighlightColors.NUMBER
        attr1[ZenScriptTypes.FLOAT_LITERAL] = ZenScriptHighlightColors.NUMBER
        attr1[ZenScriptTypes.DOUBLE_LITERAL] = ZenScriptHighlightColors.NUMBER
        attr1[ZenScriptTypes.STRING_LITERAL] = ZenScriptHighlightColors.STRING

        // token
        attr1[TokenType.BAD_CHARACTER] = HighlighterColors.BAD_CHARACTER;

        attr1[ZenScriptTypes.PAREN_CLOSE] = ZenScriptHighlightColors.PARENTHESES
        attr1[ZenScriptTypes.PAREN_OPEN] = ZenScriptHighlightColors.PARENTHESES

        attr1[ZenScriptTypes.BRACE_OPEN] = ZenScriptHighlightColors.BRACKETS
        attr1[ZenScriptTypes.BRACE_CLOSE] = ZenScriptHighlightColors.BRACKETS

        attr1[ZenScriptTypes.BRACK_OPEN] = ZenScriptHighlightColors.BRACES
        attr1[ZenScriptTypes.BRACK_OPEN] = ZenScriptHighlightColors.BRACES

        attr1[ZenScriptTypes.COMMA] = ZenScriptHighlightColors.COMMA
        attr1[ZenScriptTypes.DOT] = ZenScriptHighlightColors.DOT
        attr1[ZenScriptTypes.SEMICOLON] = ZenScriptHighlightColors.SEMICOLON


        attr1[ZenScriptTypes.BRACKET_HANDLER_LITERAL] = ZenScriptHighlightColors.BRACKET_HANDLER

        attr1[ZenScriptTypes.LINE_COMMENT] = ZenScriptHighlightColors.LINE_COMMENT
        attr1[ZenScriptTypes.BLOCK_COMMENT] = ZenScriptHighlightColors.BLOCK_COMMENT
        attr1[ZenScriptTypes.DOC_COMMENT] = ZenScriptHighlightColors.DOC_COMMENT

    }

    override fun getHighlightingLexer(): Lexer {
        return ZenScriptLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(attr1[tokenType], attr2[tokenType])
    }
}

