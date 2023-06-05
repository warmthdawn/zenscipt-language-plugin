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

public class ZenScriptIfStatementImpl extends ZenScriptStatementImpl implements ZenScriptIfStatement {

  public ZenScriptIfStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitIfStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ZenScriptStatement> getStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptStatement.class);
  }

  @Override
  @Nullable
  public ZenScriptExpression getCondition() {
    return findChildByClass(ZenScriptExpression.class);
  }

  @Override
  @Nullable
  public ZenScriptStatement getThenBody() {
    List<ZenScriptStatement> p1 = getStatementList();
    return p1.size() < 1 ? null : p1.get(0);
  }

  @Override
  @Nullable
  public ZenScriptStatement getElseBody() {
    List<ZenScriptStatement> p1 = getStatementList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}
