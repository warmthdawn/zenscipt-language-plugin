// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;

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

  public void visitArrayTypeRef(@NotNull ZenScriptArrayTypeRef o) {
    visitTypeRef(o);
  }

  public void visitAssignmentExpression(@NotNull ZenScriptAssignmentExpression o) {
    visitBinaryExpression(o);
  }

  public void visitBinaryExpression(@NotNull ZenScriptBinaryExpression o) {
    visitExpression(o);
  }

  public void visitBlockStatement(@NotNull ZenScriptBlockStatement o) {
    visitStatement(o);
    // visitCodeBlock(o);
  }

  public void visitBracketHandlerLiteral(@NotNull ZenScriptBracketHandlerLiteral o) {
    visitLiteralExpression(o);
  }

  public void visitBreakStatement(@NotNull ZenScriptBreakStatement o) {
    visitStatement(o);
  }

  public void visitCallExpression(@NotNull ZenScriptCallExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitClassDeclaration(@NotNull ZenScriptClassDeclaration o) {
    visitClass(o);
  }

  public void visitClassTypeRef(@NotNull ZenScriptClassTypeRef o) {
    visitTypeRef(o);
    // visitReference(o);
  }

  public void visitCompareExpression(@NotNull ZenScriptCompareExpression o) {
    visitBinaryExpression(o);
  }

  public void visitConditionalExpression(@NotNull ZenScriptConditionalExpression o) {
    visitExpression(o);
  }

  public void visitConstructorDeclaration(@NotNull ZenScriptConstructorDeclaration o) {
    visitMember(o);
    // visitFunction(o);
  }

  public void visitContinueStatement(@NotNull ZenScriptContinueStatement o) {
    visitStatement(o);
  }

  public void visitExpandFunctionDeclaration(@NotNull ZenScriptExpandFunctionDeclaration o) {
    visitNamedElement(o);
    // visitMember(o);
    // visitFunction(o);
  }

  public void visitExpression(@NotNull ZenScriptExpression o) {
    visitPsiElement(o);
  }

  public void visitExpressionStatement(@NotNull ZenScriptExpressionStatement o) {
    visitStatement(o);
  }

  public void visitForeachStatement(@NotNull ZenScriptForeachStatement o) {
    visitStatement(o);
    // visitLoopStatement(o);
  }

  public void visitForeachVariableDeclaration(@NotNull ZenScriptForeachVariableDeclaration o) {
    visitNamedElement(o);
  }

  public void visitFunctionBody(@NotNull ZenScriptFunctionBody o) {
    visitCodeBlock(o);
  }

  public void visitFunctionDeclaration(@NotNull ZenScriptFunctionDeclaration o) {
    visitNamedElement(o);
    // visitMember(o);
    // visitFunction(o);
  }

  public void visitFunctionLiteral(@NotNull ZenScriptFunctionLiteral o) {
    visitLiteralExpression(o);
    // visitFunction(o);
  }

  public void visitFunctionTypeRef(@NotNull ZenScriptFunctionTypeRef o) {
    visitTypeRef(o);
  }

  public void visitIdentifier(@NotNull ZenScriptIdentifier o) {
    visitPsiElement(o);
  }

  public void visitIfStatement(@NotNull ZenScriptIfStatement o) {
    visitStatement(o);
  }

  public void visitImportDeclaration(@NotNull ZenScriptImportDeclaration o) {
    visitReference(o);
    // visitNamedElement(o);
    // visitPsiPolyVariantReference(o);
  }

  public void visitImportList(@NotNull ZenScriptImportList o) {
    visitPsiElement(o);
  }

  public void visitInitializerOrDefault(@NotNull ZenScriptInitializerOrDefault o) {
    visitPsiElement(o);
  }

  public void visitInstanceOfExpression(@NotNull ZenScriptInstanceOfExpression o) {
    visitExpression(o);
  }

  public void visitListTypeRef(@NotNull ZenScriptListTypeRef o) {
    visitTypeRef(o);
  }

  public void visitLiteralExpression(@NotNull ZenScriptLiteralExpression o) {
    visitExpression(o);
  }

  public void visitLocalAccessExpression(@NotNull ZenScriptLocalAccessExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitMapEntry(@NotNull ZenScriptMapEntry o) {
    visitPsiElement(o);
  }

  public void visitMapLiteral(@NotNull ZenScriptMapLiteral o) {
    visitLiteralExpression(o);
  }

  public void visitMapTypeRef(@NotNull ZenScriptMapTypeRef o) {
    visitTypeRef(o);
  }

  public void visitMemberAccessExpression(@NotNull ZenScriptMemberAccessExpression o) {
    visitExpression(o);
    // visitReference(o);
  }

  public void visitMemberName(@NotNull ZenScriptMemberName o) {
    visitIdentifier(o);
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
    visitNamedElement(o);
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

  public void visitPrimitiveTypeRef(@NotNull ZenScriptPrimitiveTypeRef o) {
    visitTypeRef(o);
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
    visitStatement(o);
  }

  public void visitScriptBody(@NotNull ZenScriptScriptBody o) {
    visitCodeBlock(o);
  }

  public void visitStatement(@NotNull ZenScriptStatement o) {
    visitPsiElement(o);
  }

  public void visitTypeCastExpression(@NotNull ZenScriptTypeCastExpression o) {
    visitExpression(o);
  }

  public void visitTypeRef(@NotNull ZenScriptTypeRef o) {
    visitPsiElement(o);
  }

  public void visitUnaryExpression(@NotNull ZenScriptUnaryExpression o) {
    visitExpression(o);
  }

  public void visitVariableDeclaration(@NotNull ZenScriptVariableDeclaration o) {
    visitStatement(o);
    // visitMember(o);
    // visitNamedElement(o);
  }

  public void visitWhileStatement(@NotNull ZenScriptWhileStatement o) {
    visitStatement(o);
    // visitLoopStatement(o);
  }

  public void visitXorExpression(@NotNull ZenScriptXorExpression o) {
    visitBinaryExpression(o);
  }

  public void visitClass(@NotNull ZenScriptClass o) {
    visitPsiElement(o);
  }

  public void visitCodeBlock(@NotNull ZenScriptCodeBlock o) {
    visitPsiElement(o);
  }

  public void visitMember(@NotNull ZenScriptMember o) {
    visitPsiElement(o);
  }

  public void visitNamedElement(@NotNull ZenScriptNamedElement o) {
    visitPsiElement(o);
  }

  public void visitReference(@NotNull ZenScriptReference o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
