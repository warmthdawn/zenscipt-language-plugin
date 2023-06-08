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
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

public class ZenScriptForeachStatementImpl extends ZenScriptStatementImpl implements ZenScriptForeachStatement {

  public ZenScriptForeachStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
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
  public List<ZenScriptForeachVariableDeclaration> getEntries() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptForeachVariableDeclaration.class);
  }

  @Override
  @Nullable
  public ZenScriptExpression getIterTarget() {
    return findChildByClass(ZenScriptExpression.class);
  }

  @Override
  @Nullable
  public ZenScriptStatement getBody() {
    return findChildByClass(ZenScriptStatement.class);
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
    return ZenScriptImplUtil.processDeclarations(this, processor, state, lastParent, place);
  }

}
