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

public class ZenScriptExpandFunctionDeclarationImpl extends ZenScriptNamedElementImpl implements ZenScriptExpandFunctionDeclaration {

  public ZenScriptExpandFunctionDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitExpandFunctionDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ZenScriptFunctionBody getFunctionBody() {
    return findChildByClass(ZenScriptFunctionBody.class);
  }

  @Override
  @Nullable
  public ZenScriptIdentifier getIdentifier() {
    return findChildByClass(ZenScriptIdentifier.class);
  }

  @Override
  @Nullable
  public ZenScriptParameters getParameters() {
    return findChildByClass(ZenScriptParameters.class);
  }

  @Override
  @NotNull
  public List<ZenScriptTypeRef> getTypeRefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptTypeRef.class);
  }

  @Override
  @Nullable
  public ZenScriptTypeRef getReturnType() {
    List<ZenScriptTypeRef> p1 = getTypeRefList();
    return p1.size() < 2 ? null : p1.get(1);
  }

  @Override
  @Nullable
  public ZenScriptTypeRef getExpandTarget() {
    List<ZenScriptTypeRef> p1 = getTypeRefList();
    return p1.size() < 1 ? null : p1.get(0);
  }

}
