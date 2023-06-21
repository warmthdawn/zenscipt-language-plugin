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

public class ZenScriptClassDeclarationImpl extends ZenScriptClassImpl implements ZenScriptClassDeclaration {

  public ZenScriptClassDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitClassDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ZenScriptQualifiedName getQualifiedName() {
    return findChildByClass(ZenScriptQualifiedName.class);
  }

  @Override
  @NotNull
  public List<ZenScriptVariableDeclaration> getVariables() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptVariableDeclaration.class);
  }

  @Override
  @NotNull
  public List<ZenScriptConstructorDeclaration> getConstructors() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptConstructorDeclaration.class);
  }

  @Override
  @NotNull
  public List<ZenScriptFunctionDeclaration> getFunctions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptFunctionDeclaration.class);
  }

}
