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

public class ZenScriptImportDeclarationImpl extends ZenScriptNamedElementImpl implements ZenScriptImportDeclaration {

  public ZenScriptImportDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitImportDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ZenScriptImportReference getImportReference() {
    return findChildByClass(ZenScriptImportReference.class);
  }

  @Override
  @Nullable
  public ZenScriptIdentifier getIdentifier() {
    return ZenScriptImplUtil.getIdentifier(this);
  }

  @Override
  @NotNull
  public ZenScriptImportDeclaration setName(@NotNull String name) {
    return ZenScriptImplUtil.setName(this, name);
  }

  @Override
  @Nullable
  public ZenScriptIdentifier getAlias() {
    return findChildByClass(ZenScriptIdentifier.class);
  }

}
