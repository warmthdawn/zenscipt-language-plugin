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

public class ZenScriptParameterImpl extends ZenScriptNamedElementImpl implements ZenScriptParameter {

  public ZenScriptParameterImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitParameter(this);
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
  public ZenScriptType getType() {
    return findChildByClass(ZenScriptType.class);
  }

  @Override
  @Nullable
  public ZenScriptInitializerOrDefault getInitializer() {
    return findChildByClass(ZenScriptInitializerOrDefault.class);
  }

}
