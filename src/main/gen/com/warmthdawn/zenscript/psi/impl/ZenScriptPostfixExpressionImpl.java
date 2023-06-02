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

public class ZenScriptPostfixExpressionImpl extends ASTWrapperPsiElement implements ZenScriptPostfixExpression {

  public ZenScriptPostfixExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitPostfixExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ZenScriptArguments> getArgumentsList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptArguments.class);
  }

  @Override
  @NotNull
  public List<ZenScriptExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptExpression.class);
  }

  @Override
  @NotNull
  public List<ZenScriptIdentifier> getIdentifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptIdentifier.class);
  }

  @Override
  @NotNull
  public ZenScriptPrimaryExpression getPrimaryExpression() {
    return findNotNullChildByClass(ZenScriptPrimaryExpression.class);
  }

  @Override
  @NotNull
  public List<ZenScriptTypeLiteral> getTypeLiteralList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptTypeLiteral.class);
  }

}
