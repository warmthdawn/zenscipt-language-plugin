package com.warmthdawn.zenscript.psi

import com.intellij.psi.tree.TokenSet
import com.warmthdawn.zenscript.psi.ZenScriptTypes.*

object ZenScriptTokenSet {
    val IDENTIFIERS: TokenSet = TokenSet.create(ID)
    val COMMENTS: TokenSet = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT, DOC_COMMENT)
    val HIDDEN_TOKENS: TokenSet = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT, DOC_COMMENT, PREPROCESSOR, NEW_LINE)


    val KEYWORDS: TokenSet = TokenSet.create(
        K_VAR, K_VAL, K_GLOBAL, K_STATIC,
        K_IMPORT, K_FUNCTION,
        K_AS, K_TO, K_IN, K_HAS, K_INSTANCEOF,
        K_THIS, K_SUPER,
        K_ANY, K_BYTE, K_SHORT, K_INT, K_LONG, K_FLOAT, K_DOUBLE, K_BOOL, K_VOID, K_STRING,
        K_TRUE, K_FALSE, K_NULL,

        K_IF, K_ELSE, K_FOR, K_DO, K_WHILE, K_BREAK, K_CONTINUE, K_RETURN,

        K_ZEN_CLASS, K_ZEN_CONSTRUCTOR, K_EXPAND,
    )

    val OPERATORS: TokenSet = TokenSet.create(
        OP_ADD, OP_SUB, OP_MUL, OP_DIV, OP_MOD, OP_CAT,

        OP_XOR, OP_AND, OP_OR,

        OP_COLON, OP_QUEST, OP_BACKTICK, OP_DOLLAR,

        OP_NOT, OP_LESS, OP_GREATER, OP_AND_AND, OP_OR_OR,
        OP_EQUAL, OP_NOT_EQUAL, OP_LESS_EQUAL, OP_GREATER_EQUAL,

        OP_ASSIGN, OP_ADD_ASSIGN, OP_SUB_ASSIGN, OP_MUL_ASSIGN, OP_DIV_ASSIGN,
        OP_MOD_ASSIGN, OP_XOR_ASSIGN, OP_AND_ASSIGN, OP_OR_ASSIGN, OP_CAT_ASSIGN,

        OP_DOT_DOT,
    )
}
