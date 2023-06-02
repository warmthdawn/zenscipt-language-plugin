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

public class ZenScriptExpandFunctionDeclarationImpl extends ASTWrapperPsiElement implements ZenScriptExpandFunctionDeclaration {

  public ZenScriptExpandFunctionDeclarationImpl(@NotNull ASTNode node) {
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
  @NotNull
  public ZenScriptFunctionBody getFunctionBody() {
    return findNotNullChildByClass(ZenScriptFunctionBody.class);
  }

  @Override
  @NotNull
  public ZenScriptIdentifier getIdentifier() {
    return findNotNullChildByClass(ZenScriptIdentifier.class);
  }

  @Override
  @NotNull
  public List<ZenScriptParameter> getParameterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptParameter.class);
  }

  @Override
  @NotNull
  public List<ZenScriptTypeLiteral> getTypeLiteralList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptTypeLiteral.class);
  }

}
