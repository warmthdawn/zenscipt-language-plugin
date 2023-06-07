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

public class ZenScriptFunctionTypeImpl extends ZenScriptTypeImpl implements ZenScriptFunctionType {

  public ZenScriptFunctionTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitFunctionType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ZenScriptType> getTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptType.class);
  }

  @Override
  @Nullable
  public ZenScriptType getReturnType() {
    return ZenScriptImplUtil.getReturnType(this);
  }

  @Override
  @NotNull
  public List<ZenScriptType> getParamsType() {
    return ZenScriptImplUtil.getParamsType(this);
  }

}
