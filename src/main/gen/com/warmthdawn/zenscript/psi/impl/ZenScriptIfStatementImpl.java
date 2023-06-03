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

public class ZenScriptIfStatementImpl extends ASTWrapperPsiElement implements ZenScriptIfStatement {

  public ZenScriptIfStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitIfStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ZenScriptExpression getExpression() {
    return findChildByClass(ZenScriptExpression.class);
  }

  @Override
  @NotNull
  public List<ZenScriptStatement> getStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptStatement.class);
  }

}
