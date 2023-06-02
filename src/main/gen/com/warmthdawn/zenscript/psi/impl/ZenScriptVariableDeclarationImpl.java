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

public class ZenScriptVariableDeclarationImpl extends ASTWrapperPsiElement implements ZenScriptVariableDeclaration {

  public ZenScriptVariableDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitVariableDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ZenScriptIdentifier getIdentifier() {
    return findNotNullChildByClass(ZenScriptIdentifier.class);
  }

  @Override
  @Nullable
  public ZenScriptInitializer getInitializer() {
    return findChildByClass(ZenScriptInitializer.class);
  }

  @Override
  @Nullable
  public ZenScriptTypeLiteral getTypeLiteral() {
    return findChildByClass(ZenScriptTypeLiteral.class);
  }

}
