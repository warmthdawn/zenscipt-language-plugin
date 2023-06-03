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

public class ZenScriptClassTypeImpl extends ZenScriptTypeImpl implements ZenScriptClassType {

  public ZenScriptClassTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitClassType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ZenScriptIdentifier getIdentifier() {
    return findChildByClass(ZenScriptIdentifier.class);
  }

  @Override
  @Nullable
  public ZenScriptQualifiedClassType getQualifiedClassType() {
    return findChildByClass(ZenScriptQualifiedClassType.class);
  }

}
