// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class ZenScriptVisitor extends PsiElementVisitor {

  public void visitAddExpression(@NotNull ZenScriptAddExpression o) {
    visitBinaryExpression(o);
  }

  public void visitAndAndExpression(@NotNull ZenScriptAndAndExpression o) {
    visitBinaryExpression(o);
  }

  public void visitAndExpression(@NotNull ZenScriptAndExpression o) {
    visitBinaryExpression(o);
  }

  public void visitArguments(@NotNull ZenScriptArguments o) {
    visitPsiElement(o);
  }

  public void visitArrayIndexExpression(@NotNull ZenScriptArrayIndexExpression o) {
    visitExpression(o);
  }

  public void visitArrayLiteral(@NotNull ZenScriptArrayLiteral o) {
    visitLiteralExpression(o);
  }

  public void visitArrayType(@NotNull ZenScriptArrayType o) {
    visitType(o);
  }

  public void visitAssignmentExpression(@NotNull ZenScriptAssignmentExpression o) {
    visitBinaryExpression(o);
  }

  public void visitBinaryExpression(@NotNull ZenScriptBinaryExpression o) {
    visitExpression(o);
  }

  public void visitBlockStatement(@NotNull ZenScriptBlockStatement o) {
    visitPsiElement(o);
  }

  public void visitBracketHandlerLiteral(@NotNull ZenScriptBracketHandlerLiteral o) {
    visitLiteralExpression(o);
  }

  public void visitBreakStatement(@NotNull ZenScriptBreakStatement o) {
    visitPsiElement(o);
  }

  public void visitCallExpression(@NotNull ZenScriptCallExpression o) {
    visitExpression(o);
  }

  public void visitClassDeclaration(@NotNull ZenScriptClassDeclaration o) {
    visitPsiElement(o);
  }

  public void visitClassType(@NotNull ZenScriptClassType o) {
    visitType(o);
  }

  public void visitCompareExpression(@NotNull ZenScriptCompareExpression o) {
    visitBinaryExpression(o);
  }

  public void visitConditionalExpression(@NotNull ZenScriptConditionalExpression o) {
    visitExpression(o);
  }

  public void visitConstructorBody(@NotNull ZenScriptConstructorBody o) {
    visitPsiElement(o);
  }

  public void visitConstructorDeclaration(@NotNull ZenScriptConstructorDeclaration o) {
    visitPsiElement(o);
  }

  public void visitContinueStatement(@NotNull ZenScriptContinueStatement o) {
    visitPsiElement(o);
  }

  public void visitExpandFunctionDeclaration(@NotNull ZenScriptExpandFunctionDeclaration o) {
    visitPsiElement(o);
  }

  public void visitExpression(@NotNull ZenScriptExpression o) {
    visitPsiElement(o);
  }

  public void visitExpressionStatement(@NotNull ZenScriptExpressionStatement o) {
    visitPsiElement(o);
  }

  public void visitForeachBody(@NotNull ZenScriptForeachBody o) {
    visitPsiElement(o);
  }

  public void visitForeachStatement(@NotNull ZenScriptForeachStatement o) {
    visitPsiElement(o);
  }

  public void visitFunctionBody(@NotNull ZenScriptFunctionBody o) {
    visitPsiElement(o);
  }

  public void visitFunctionDeclaration(@NotNull ZenScriptFunctionDeclaration o) {
    visitPsiElement(o);
  }

  public void visitFunctionLiteral(@NotNull ZenScriptFunctionLiteral o) {
    visitLiteralExpression(o);
  }

  public void visitFunctionType(@NotNull ZenScriptFunctionType o) {
    visitType(o);
  }

  public void visitIdentifier(@NotNull ZenScriptIdentifier o) {
    visitPsiElement(o);
  }

  public void visitIfStatement(@NotNull ZenScriptIfStatement o) {
    visitPsiElement(o);
  }

  public void visitImportDeclaration(@NotNull ZenScriptImportDeclaration o) {
    visitPsiElement(o);
  }

  public void visitImportList(@NotNull ZenScriptImportList o) {
    visitPsiElement(o);
  }

  public void visitInstanceOfExpression(@NotNull ZenScriptInstanceOfExpression o) {
    visitExpression(o);
  }

  public void visitListType(@NotNull ZenScriptListType o) {
    visitType(o);
  }

  public void visitLiteralExpression(@NotNull ZenScriptLiteralExpression o) {
    visitExpression(o);
  }

  public void visitLocalAccessExpression(@NotNull ZenScriptLocalAccessExpression o) {
    visitExpression(o);
  }

  public void visitMapEntry(@NotNull ZenScriptMapEntry o) {
    visitPsiElement(o);
  }

  public void visitMapLiteral(@NotNull ZenScriptMapLiteral o) {
    visitLiteralExpression(o);
  }

  public void visitMapType(@NotNull ZenScriptMapType o) {
    visitType(o);
  }

  public void visitMemberAccessExpression(@NotNull ZenScriptMemberAccessExpression o) {
    visitExpression(o);
  }

  public void visitMulExpression(@NotNull ZenScriptMulExpression o) {
    visitBinaryExpression(o);
  }

  public void visitOrExpression(@NotNull ZenScriptOrExpression o) {
    visitBinaryExpression(o);
  }

  public void visitOrOrExpression(@NotNull ZenScriptOrOrExpression o) {
    visitBinaryExpression(o);
  }

  public void visitParameter(@NotNull ZenScriptParameter o) {
    visitPsiElement(o);
  }

  public void visitParameters(@NotNull ZenScriptParameters o) {
    visitPsiElement(o);
  }

  public void visitParenExpression(@NotNull ZenScriptParenExpression o) {
    visitExpression(o);
  }

  public void visitPostfixExpression(@NotNull ZenScriptPostfixExpression o) {
    visitExpression(o);
  }

  public void visitPrimitiveLiteral(@NotNull ZenScriptPrimitiveLiteral o) {
    visitLiteralExpression(o);
  }

  public void visitPrimitiveType(@NotNull ZenScriptPrimitiveType o) {
    visitType(o);
  }

  public void visitQualifiedClassType(@NotNull ZenScriptQualifiedClassType o) {
    visitType(o);
  }

  public void visitQualifiedName(@NotNull ZenScriptQualifiedName o) {
    visitPsiElement(o);
  }

  public void visitQualifier(@NotNull ZenScriptQualifier o) {
    visitPsiElement(o);
  }

  public void visitRangeExpression(@NotNull ZenScriptRangeExpression o) {
    visitBinaryExpression(o);
  }

  public void visitReturnStatement(@NotNull ZenScriptReturnStatement o) {
    visitPsiElement(o);
  }

  public void visitScriptBody(@NotNull ZenScriptScriptBody o) {
    visitPsiElement(o);
  }

  public void visitStatement(@NotNull ZenScriptStatement o) {
    visitPsiElement(o);
  }

  public void visitType(@NotNull ZenScriptType o) {
    visitPsiElement(o);
  }

  public void visitTypeCastExpression(@NotNull ZenScriptTypeCastExpression o) {
    visitExpression(o);
  }

  public void visitUnaryExpression(@NotNull ZenScriptUnaryExpression o) {
    visitExpression(o);
  }

  public void visitVariableDeclaration(@NotNull ZenScriptVariableDeclaration o) {
    visitPsiElement(o);
  }

  public void visitWhileStatement(@NotNull ZenScriptWhileStatement o) {
    visitPsiElement(o);
  }

  public void visitXorExpression(@NotNull ZenScriptXorExpression o) {
    visitBinaryExpression(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
