@file:JvmName("ZenScriptImplUtil")

package com.warmthdawn.zenscript.psi

import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.UnfairTextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import com.warmthdawn.zenscript.psi.impl.ZenScriptPrimitiveTypeRefImpl
import com.warmthdawn.zenscript.util.createIdentifierFromText
import com.warmthdawn.zenscript.util.hasStaticModifier


private val unaryOp = TokenSet.create(ZenScriptTypes.OP_SUB, ZenScriptTypes.OP_NOT)
private val binaryOp = TokenSet.create(
        ZenScriptTypes.OP_DOT_DOT, ZenScriptTypes.K_TO,
        ZenScriptTypes.OP_ADD, ZenScriptTypes.OP_CAT, ZenScriptTypes.OP_SUB,
        ZenScriptTypes.OP_DIV, ZenScriptTypes.OP_MOD, ZenScriptTypes.OP_MUL,
        ZenScriptTypes.K_HAS, ZenScriptTypes.K_IN, ZenScriptTypes.OP_EQUAL, ZenScriptTypes.OP_GREATER,
        ZenScriptTypes.OP_GREATER_EQUAL, ZenScriptTypes.OP_LESS, ZenScriptTypes.OP_LESS_EQUAL, ZenScriptTypes.OP_NOT_EQUAL,
        ZenScriptTypes.OP_ADD_ASSIGN,
        ZenScriptTypes.OP_AND_ASSIGN,
        ZenScriptTypes.OP_ASSIGN,
        ZenScriptTypes.OP_CAT_ASSIGN,
        ZenScriptTypes.OP_DIV_ASSIGN,
        ZenScriptTypes.OP_MOD_ASSIGN,
        ZenScriptTypes.OP_MUL_ASSIGN,
        ZenScriptTypes.OP_OR_ASSIGN,
        ZenScriptTypes.OP_SUB_ASSIGN,
        ZenScriptTypes.OP_XOR_ASSIGN,
        ZenScriptTypes.OP_OR,
        ZenScriptTypes.OP_AND,
        ZenScriptTypes.OP_XOR,
        ZenScriptTypes.OP_AND_AND,
        ZenScriptTypes.OP_OR_OR,
)

fun getOperator(unaryExpr: ZenScriptUnaryExpression): IElementType {
    return unaryExpr.node.findChildByType(unaryOp)!!.elementType
}

fun getOperator(binaryExpr: ZenScriptBinaryExpression): IElementType {
    return binaryExpr.node.findChildByType(binaryOp)!!.elementType
}


fun getIdentifier(classDec: ZenScriptClassDeclaration): ZenScriptIdentifier? {
    return classDec.qualifiedName?.identifier
}

fun getIdentifier(ctor: ZenScriptConstructorDeclaration): ZenScriptIdentifier? {
    return (ctor.parent as? ZenScriptClassDeclaration)?.identifier
}

fun getReturnType(ctor: ZenScriptConstructorDeclaration): ZenScriptTypeRef? = null
fun getReturnType(funcType: ZenScriptFunctionTypeRef): ZenScriptTypeRef? = funcType.typeRefList.lastOrNull()
fun getParamsType(funcType: ZenScriptFunctionTypeRef): List<ZenScriptTypeRef> = funcType.typeRefList.let {
    if (it.size > 1) {
        it.subList(0, it.size - 1)
    } else {
        emptyList()
    }
}

fun getRangeInElement(memberAccessExpr: ZenScriptMemberAccessExpression): TextRange {
    val nameRange = memberAccessExpr.identifier!!.textRange
    val exprRange = memberAccessExpr.textRange

    return UnfairTextRange(nameRange.startOffset - exprRange.startOffset, nameRange.endOffset - exprRange.startOffset)
}


fun processDeclarations(forEachStmt: ZenScriptForeachStatement, processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {
    if (lastParent != forEachStmt.body) {
        return true
    }
    processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, forEachStmt)
    for (variable in forEachStmt.entries) {
        if (!processor.execute(variable, state)) {
            processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
            return false
        }
    }
    processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
    return true
}

fun getName(importDecl: ZenScriptImportDeclaration): String? {
    val identifier = importDecl.alias ?: importDecl.importReference?.qualifiedName?.identifier
    return identifier?.text
}