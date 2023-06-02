package com.warmthdawn.zenscript.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.warmthdawn.zenscript.ZSLanguage
import com.warmthdawn.zenscript.parser.ZenScriptParser
import com.warmthdawn.zenscript.psi.ZenScriptTokenSet
import com.warmthdawn.zenscript.psi.ZenScriptTypes

class ZenScriptParserDefinition : ParserDefinition {
    private val _file = IFileElementType(ZSLanguage)

    override fun createLexer(project: Project): Lexer {
        return ZenScriptLexerAdapter()
    }

    override fun createParser(project: Project?): PsiParser {
        return ZenScriptParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return _file
    }

    override fun getCommentTokens(): TokenSet {
        return ZenScriptTokenSet.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return ZenScriptTokenSet.IDENTIFIERS
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return ZenScriptTypes.Factory.createElement(node)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return ZenScriptFile(viewProvider)
    }
}