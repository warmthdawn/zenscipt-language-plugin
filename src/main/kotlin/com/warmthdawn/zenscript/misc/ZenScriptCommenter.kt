package com.warmthdawn.zenscript.misc

import com.intellij.lang.CodeDocumentationAwareCommenter
import com.intellij.lang.Commenter
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.tree.IElementType
import com.warmthdawn.zenscript.psi.ZenScriptTypes

class ZenScriptCommenter : CodeDocumentationAwareCommenter {
    override fun getLineCommentPrefix(): String {
        return "//"
    }

    override fun getBlockCommentPrefix(): String {
        return "/*"
    }

    override fun getBlockCommentSuffix(): String {
        return "*/"
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        return null
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        return null
    }

    override fun getLineCommentTokenType(): IElementType? {
        return ZenScriptTypes.LINE_COMMENT
    }

    override fun getBlockCommentTokenType(): IElementType? {
        return ZenScriptTypes.BLOCK_COMMENT
    }

    override fun getDocumentationCommentTokenType(): IElementType? {
        return ZenScriptTypes.DOC_COMMENT
    }

    override fun getDocumentationCommentPrefix(): String? {
        return "/**"
    }

    override fun getDocumentationCommentLinePrefix(): String? {
        return "*"
    }

    override fun getDocumentationCommentSuffix(): String? {
        return null
    }

    override fun isDocumentationComment(element: PsiComment?): Boolean {
        return element is PsiDocCommentBase
    }
}