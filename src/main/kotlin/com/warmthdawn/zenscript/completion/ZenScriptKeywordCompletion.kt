package com.warmthdawn.zenscript.completion

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.psi.*
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.warmthdawn.zenscript.psi.*

class ZenScriptKeywordCompletion(
    private val position: PsiElement,
    private val result: CompletionResultSet,
) {
    companion object {
        private val INSIDE_IMPORT_LIST = psiElement().inside(psiElement(ZenScriptImportList::class.java))
        private val AT_EXPRESSION_STMT_STARING =
            psiElement().insideStarting(psiElement(ZenScriptExpressionStatement::class.java))


        private val INSIDE_FUNCTIONS = psiElement().inside(psiElement(ZenScriptFunction::class.java))

        private val AT_TOP_LEVEL = not(
            psiElement().inside(
                or(
                    psiElement(ZenScriptClassDeclaration::class.java),
                    psiElement(ZenScriptFunctionDeclaration::class.java),
                    psiElement(ZenScriptExpandFunctionDeclaration::class.java)
                )
            )
        )
    }

    private val prevLeaf: PsiElement? = PsiTreeUtil.prevCodeLeaf(position)
    fun addAllKeywords() {

        if (PsiTreeUtil.getNonStrictParentOfType(
                position, ZenScriptLiteralExpression::class.java, PsiComment::class.java
            ) != null
        ) return

        if (psiElement().afterLeaf(".").accepts(position)) {
            return
        }

        val isTopLevel = AT_TOP_LEVEL.accepts(position)
        if (isTopLevel) {
            addImportKeyword()
            addTopLevelKeywords()
        }

        addClassMemberKeywords()
        addTypeAsKeyword()
        addTypeKeywords()


        addStatementKeywords(isTopLevel)

        addExpressionKeywords()

    }

    private fun isStatementPos(): Boolean {
        if (psiElement().inside(psiElement(ZenScriptClassDeclaration::class.java))
                .andNot(INSIDE_FUNCTIONS).accepts(position)
        ) {
            return false;
        }

        if (isEndOfBlock()) {
            return true
        }

        val stmt = PsiTreeUtil.getParentOfType(position, ZenScriptStatement::class.java) ?: return false

        val parentStmt = stmt.parent
        if (parentStmt is ZenScriptIfStatement) {
            return stmt == parentStmt.thenBody || stmt == parentStmt.elseBody
        }

        if (parentStmt is ZenScriptLoopStatement) {
            return stmt == parentStmt.body
        }

        return false

    }

    private fun addStatementKeywords(isTopLevel: Boolean) {
        if (!isStatementPos()) {
            return
        }

        addKeywords("for", "if", "while", "val", "var")
        if (!isTopLevel) {
            addKeyword("return")
        }

        // break & continue
        val loop = PsiTreeUtil.getParentOfType(
            position,
            ZenScriptLoopStatement::class.java,
            true,
            ZenScriptFunctionBody::class.java
        )

        if (loop != null && PsiTreeUtil.isAncestor(loop.body, position, false)) {
            addKeywords("break", "continue")
            return
        }

        // else
        if (psiElement().withText(string().oneOf(";", "}"))
                .withSuperParent(2, psiElement(ZenScriptIfStatement::class.java))
                .accepts(position)
        ) {
            addKeyword("else")
        }

    }

    private fun acceptType(expr: ZenScriptExpression, type: String): Boolean {
        //TODO()
        return true
    }

    private fun hasOperator(expr: ZenScriptExpression, op: String): Boolean {
        return true
    }

    private fun addExpressionKeywords() {
        if (psiElement().insideStarting(psiElement(ZenScriptLocalAccessExpression::class.java)).accepts(position)) {
            val expr = position.parent?.parent
            if (expr is ZenScriptExpression && expr.parent !is ZenScriptExpressionStatement) {
                if (acceptType(expr, "null")) {
                    addKeyword("null")
                }
                if (acceptType(expr, "bool")) {

                    addKeywords("true", "false")
                }
                if (acceptType(expr, "function")) {

                    addKeyword("function")
                }
            }

        }

        // to as instanceof in has
        prevLeaf ?: return
        val prevParentExpr = PsiTreeUtil.getParentOfType(prevLeaf, ZenScriptExpression::class.java) ?: return

        // ensure prev is last element
        if (prevParentExpr.endOffset != prevLeaf.endOffset) {
            return
        }

        if (hasOperator(prevParentExpr, "as")) {
            addKeyword("as")
        }

        if (hasOperator(prevParentExpr, "to")) {
            addKeyword("to")
        }

        if (hasOperator(prevParentExpr, "instanceof")) {
            addKeyword("instanceof")
        }

        if (hasOperator(prevParentExpr, "in")) {
            addKeyword("in")
        }

        if (hasOperator(prevParentExpr, "has")) {
            addKeyword("has")
        }

        // prevLeaf is in expr

    }

    private fun addTypeKeywords() {
        if (!psiElement().inside(psiElement(ZenScriptType::class.java)).accepts(position)) {
            return
        }

        if (prevLeaf != null && prevLeaf.textMatches(".")) {
            return
        }

        addKeywords("function", "any", "byte", "short", "int", "long", "float", "double", "bool", "void", "string")
    }

    /**
     * 'as' <type>
     */
    private fun addTypeAsKeyword() {
        if (prevLeaf == null) {
            return
        }
        if (!prevLeaf.textMatches("=") && PsiTreeUtil.getParentOfType(
                prevLeaf,
                ZenScriptVariableDeclaration::class.java,
                true,
                ZenScriptInitializerOrDefault::class.java
            ) != null
        ) {
            if (prevLeaf.parent is ZenScriptIdentifier) {
                addKeyword("as")
            }
            return
        }

        if (INSIDE_FUNCTIONS.accepts(prevLeaf) && !prevLeaf.textMatches("}")) {

            // skip function body
            if (!prevLeaf.textMatches("}") &&PsiTreeUtil.getParentOfType(
                    prevLeaf,
                    ZenScriptFunctionBody::class.java,
                    true,
                    ZenScriptFunction::class.java
                ) != null
            ) {
                return
            }

            if (prevLeaf.textMatches(")") && !psiElement().inside(ZenScriptConstructorDeclaration::class.java)
                    .accepts(prevLeaf)
            ) {
                addKeyword("as")
                return
            }

            val insideParameters = psiElement().inside(true, psiElement(ZenScriptParameters::class.java))
            if (prevLeaf.parent is ZenScriptIdentifier && insideParameters.accepts(prevLeaf)) {
                addKeyword("as")
            }
        }
    }

    private fun addClassMemberKeywords() {
        if (!psiElement().inside(true, psiElement(ZenScriptClassDeclaration::class.java)).accepts(position)) {
            return
        }

        if (psiElement().inside(psiElement(ZenScriptFunctionBody::class.java)).accepts(position)) {
            return
        }

        if (prevLeaf != null && prevLeaf.textMatches("static")) {
            addKeyword("function")
            return
        }

        if (!isEndOfBlock() || !AT_EXPRESSION_STMT_STARING.accepts(position)) {
            return
        }

        addKeywords("static", "val", "var", "function")
    }

    private fun addTopLevelKeywords() {
        if (!isEndOfBlock()) {
            return
        }

        val parentStmt = PsiTreeUtil.getParentOfType(position, ZenScriptStatement::class.java)
        if (parentStmt != null && parentStmt !is ZenScriptExpressionStatement) {
            return
        }

        if (AT_EXPRESSION_STMT_STARING.accepts(position)) {
            addKeywords("static", "global", "zenClass", "function", "\$expand")
            return
        }
    }

    private fun addImportKeyword() {
        // import
        if (prevLeaf != null && !INSIDE_IMPORT_LIST.accepts(prevLeaf)
        ) {
            return
        }

        if (prevLeaf != null) {
            if (isEndOfBlock()) {
                addKeyword("import")
            } else if (psiElement().inside(psiElement(ZenScriptQualifiedName::class.java)).accepts(prevLeaf)) {
                addKeyword("as")
            }
            return
        }

        addKeyword("import")
    }


    private fun isEndOfBlock(element: PsiElement): Boolean {

        val prev = PsiTreeUtil.prevCodeLeaf(element)
        if (prev == null) {
            val file = element.containingFile
            return file !is PsiCodeFragment
        }
        return isEndOfBlock(prev, element)
    }

    private var currentlyEndOfBlock: Boolean? = null
    private fun isEndOfBlock(): Boolean {
        if (currentlyEndOfBlock == null) {
            currentlyEndOfBlock = prevLeaf == null || isEndOfBlock(prevLeaf, position)
        }
        return currentlyEndOfBlock!!
    }

    private fun isEndOfBlock(prev: PsiElement, element: PsiElement): Boolean {
        if (prev is OuterLanguageElement) return true
        if (psiElement().withText(string().oneOf("{", "}", ";")).accepts(prev)) return true
        if (prev.textMatches(")")) {
            val parent = prev.parent
            return if (parent is PsiParameterList) {
//                PsiTreeUtil.getParentOfType(
//                    PsiTreeUtil.prevVisibleLeaf(element),
//                    PsiDocComment::class.java
//                ) != null
                return false
            } else !(parent is PsiExpressionList || parent is PsiTypeCastExpression
                    || parent is PsiRecordHeader)
        }
        return false
    }


    private fun addKeywords(vararg keywords: String) {
        for (keyword in keywords) {
            addKeyword(keyword)
        }
    }

    private fun addKeyword(keyword: String) {
        result.addElement(
            LookupElementBuilder.create(keyword)
                .bold()
        )
    }


}