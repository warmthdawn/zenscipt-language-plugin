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

public class ZenScriptArrayIndexExpressionImpl extends ZenScriptExpressionImpl implements ZenScriptArrayIndexExpression {

  public ZenScriptArrayIndexExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitArrayIndexExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ZenScriptExpression getExpression() {
    List<ZenScriptExpression> p1 = PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptExpression.class);
    return p1.get(0);
  }

  @Override
  @Nullable
  public ZenScriptExpression getIndex() {
    List<ZenScriptExpression> p1 = PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptExpression.class);
    return p1.size() < 2 ? null : p1.get(1);
  }

}
