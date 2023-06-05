@file:JvmName("ZenScriptImplUtil")

package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.mapInPlace
import groovyjarjarantlr.preprocessor.Preprocessor


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