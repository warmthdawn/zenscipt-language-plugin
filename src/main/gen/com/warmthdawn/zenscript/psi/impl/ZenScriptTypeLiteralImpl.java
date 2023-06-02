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

public class ZenScriptTypeLiteralImpl extends ASTWrapperPsiElement implements ZenScriptTypeLiteral {

  public ZenScriptTypeLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitTypeLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ZenScriptArraySuffix getArraySuffix() {
    return findNotNullChildByClass(ZenScriptArraySuffix.class);
  }

  @Override
  @Nullable
  public ZenScriptPrimitiveType getPrimitiveType() {
    return findChildByClass(ZenScriptPrimitiveType.class);
  }

  @Override
  @Nullable
  public ZenScriptQualifiedName getQualifiedName() {
    return findChildByClass(ZenScriptQualifiedName.class);
  }

  @Override
  @NotNull
  public List<ZenScriptTypeLiteral> getTypeLiteralList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptTypeLiteral.class);
  }

}
