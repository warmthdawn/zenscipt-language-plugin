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

public class ZenScriptForeachStatementImpl extends ASTWrapperPsiElement implements ZenScriptForeachStatement {

  public ZenScriptForeachStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitForeachStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ZenScriptExpression getExpression() {
    return findNotNullChildByClass(ZenScriptExpression.class);
  }

  @Override
  @NotNull
  public ZenScriptForeachBody getForeachBody() {
    return findNotNullChildByClass(ZenScriptForeachBody.class);
  }

  @Override
  @NotNull
  public List<ZenScriptSimpleVariable> getSimpleVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptSimpleVariable.class);
  }

}
