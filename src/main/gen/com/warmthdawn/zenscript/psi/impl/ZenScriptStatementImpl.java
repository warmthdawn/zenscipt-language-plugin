// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.warmthdawn.zenscript.psi.ZenScriptTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.warmthdawn.zenscript.psi.*;

public class ZenScriptStatementImpl extends ASTWrapperPsiElement implements ZenScriptStatement {

  public ZenScriptStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ZenScriptBlockStatement getBlockStatement() {
    return findChildByClass(ZenScriptBlockStatement.class);
  }

  @Override
  @Nullable
  public ZenScriptBreakStatement getBreakStatement() {
    return findChildByClass(ZenScriptBreakStatement.class);
  }

  @Override
  @Nullable
  public ZenScriptContinueStatement getContinueStatement() {
    return findChildByClass(ZenScriptContinueStatement.class);
  }

  @Override
  @Nullable
  public ZenScriptExpressionStatement getExpressionStatement() {
    return findChildByClass(ZenScriptExpressionStatement.class);
  }

  @Override
  @Nullable
  public ZenScriptForeachStatement getForeachStatement() {
    return findChildByClass(ZenScriptForeachStatement.class);
  }

  @Override
  @Nullable
  public ZenScriptIfStatement getIfStatement() {
    return findChildByClass(ZenScriptIfStatement.class);
  }

  @Override
  @Nullable
  public ZenScriptReturnStatement getReturnStatement() {
    return findChildByClass(ZenScriptReturnStatement.class);
  }

  @Override
  @Nullable
  public ZenScriptVariableDeclaration getVariableDeclaration() {
    return findChildByClass(ZenScriptVariableDeclaration.class);
  }

  @Override
  @Nullable
  public ZenScriptWhileStatement getWhileStatement() {
    return findChildByClass(ZenScriptWhileStatement.class);
  }

}
