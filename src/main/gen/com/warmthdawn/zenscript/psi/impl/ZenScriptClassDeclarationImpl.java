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

public class ZenScriptClassDeclarationImpl extends ASTWrapperPsiElement implements ZenScriptClassDeclaration {

  public ZenScriptClassDeclarationImpl(@NotNull ASTNode node) {
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
  @NotNull
  public List<ZenScriptConstructorDeclaration> getConstructorDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptConstructorDeclaration.class);
  }

  @Override
  @NotNull
  public List<ZenScriptFunctionDeclaration> getFunctionDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptFunctionDeclaration.class);
  }

  @Override
  @NotNull
  public ZenScriptQualifiedName getQualifiedName() {
    return findNotNullChildByClass(ZenScriptQualifiedName.class);
  }

  @Override
  @NotNull
  public List<ZenScriptVariableDeclaration> getVariableDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptVariableDeclaration.class);
  }

}
