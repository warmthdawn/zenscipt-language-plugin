package com.warmthdawn.zenscript.misc

import com.intellij.codeInsight.highlighting.PairedBraceAndAnglesMatcher
import com.intellij.codeInsight.hint.DeclarationRangeUtil
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.warmthdawn.zenscript.ZSLanguage
import com.warmthdawn.zenscript.ZSLanguageFileType
import com.warmthdawn.zenscript.psi.*

private val PAIRS = arrayOf(
    BracePair(ZenScriptTypes.BRACE_OPEN, ZenScriptTypes.BRACE_CLOSE, true),
    BracePair(ZenScriptTypes.PAREN_OPEN, ZenScriptTypes.PAREN_CLOSE, false),
    BracePair(ZenScriptTypes.BRACK_OPEN, ZenScriptTypes.BRACK_CLOSE, false),
    BracePair(ZenScriptTypes.OP_LESS, ZenScriptTypes.OP_GREATER, false),
)


private val TYPE_TOKENS_INSIDE_ANGLE_BRACKETS = TokenSet.orSet(
    ZenScriptTokenSet.COMMENTS,
    ZenScriptTokenSet.WHITE_SPACES,
    ZenScriptTokenSet.KEYWORDS,
    TokenSet.create(
        ZenScriptTypes.ID,
        ZenScriptTypes.OP_COLON,
        ZenScriptTypes.OP_MUL,
        ZenScriptTypes.OP_EQUAL,
        ZenScriptTypes.COMMA
    )

)

class ZenScriptPairedBraceMatcher :
    PairedBraceAndAnglesMatcher(
        ZenScriptPairMatcher(),
        ZSLanguage,
        ZSLanguageFileType,
        TYPE_TOKENS_INSIDE_ANGLE_BRACKETS
    ) {
    override fun lt(): IElementType = ZenScriptTypes.OP_LESS
    override fun gt(): IElementType = ZenScriptTypes.OP_GREATER
}

class ZenScriptPairMatcher : PairedBraceMatcher {


    override fun getPairs(): Array<BracePair> = PAIRS
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        if (lbraceType == ZenScriptTypes.OP_LESS) {

            return false
        }

        if (contextType is ZenScriptElementType && !isPairedBracesAllowedBeforeTypeInZS(contextType)) {
            return false
        }

        return false

    }

    private fun isPairedBracesAllowedBeforeTypeInZS(tokenType: ZenScriptElementType): Boolean {
        return ZenScriptTokenSet.COMMENTS.contains(tokenType) || ZenScriptTokenSet.WHITE_SPACES.contains(tokenType)
                || tokenType === ZenScriptTypes.SEMICOLON || tokenType === ZenScriptTypes.COMMA
                || tokenType === ZenScriptTypes.PAREN_CLOSE || tokenType === ZenScriptTypes.BRACK_CLOSE
                || tokenType === ZenScriptTypes.BRACE_CLOSE || tokenType === ZenScriptTypes.BRACE_OPEN
                || tokenType === ZenScriptTypes.DOT
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        val element = file.findElementAt(openingBraceOffset)
        if (element == null || element is PsiFile) return openingBraceOffset
        var parent = element.parent
        if (parent is ZenScriptCodeBlock) {
            parent = parent.parent
            if (parent is ZenScriptFunction) {
                val range = DeclarationRangeUtil.getDeclarationRange(parent)
                return range.startOffset
            } else if (parent is ZenScriptStatement) {
                if (parent is ZenScriptBlockStatement && parent.parent is ZenScriptStatement) {
                    parent = parent.parent
                }
                return parent.textRange.startOffset
            }
        } else if (parent is ZenScriptClassDeclaration) {
            val range = DeclarationRangeUtil.getDeclarationRange(parent)
            return range.startOffset
        }
        return openingBraceOffset
    }
}