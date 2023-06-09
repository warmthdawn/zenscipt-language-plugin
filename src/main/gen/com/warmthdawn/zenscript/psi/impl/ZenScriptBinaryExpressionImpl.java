// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.warmthdawn.zenscript.psi.ZenScriptTypes.*;
import com.warmthdawn.zenscript.psi.*;
import com.intellij.psi.tree.IElementType;

public class ZenScriptBinaryExpressionImpl extends ZenScriptExpressionImpl implements ZenScriptBinaryExpression {

  public ZenScriptBinaryExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitBinaryExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ZenScriptExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptExpression.class);
  }

  @Override
  @NotNull
  public ZenScriptExpression getLeft() {
    List<ZenScriptExpression> p1 = getExpressionList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public ZenScriptExpression getRight() {
    List<ZenScriptExpression> p1 = getExpressionList();
    return p1.size() < 2 ? null : p1.get(1);
  }

  @Override
  @NotNull
  public IElementType getOperator() {
    return ZenScriptImplUtil.getOperator(this);
  }

}
