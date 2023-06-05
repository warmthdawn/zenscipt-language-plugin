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

public class ZenScriptScriptBodyImpl extends ASTWrapperPsiElement implements ZenScriptScriptBody {

  public ZenScriptScriptBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitScriptBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ZenScriptClassDeclaration> getClassDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptClassDeclaration.class);
  }

  @Override
  @NotNull
  public List<ZenScriptExpandFunctionDeclaration> getExpandFunctionDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptExpandFunctionDeclaration.class);
  }

  @Override
  @NotNull
  public List<ZenScriptFunctionDeclaration> getFunctionDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptFunctionDeclaration.class);
  }

  @Override
  @NotNull
  public ZenScriptPreprocessors getPreprocessors() {
    return findNotNullChildByClass(ZenScriptPreprocessors.class);
  }

  @Override
  @NotNull
  public List<ZenScriptStatement> getStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptStatement.class);
  }

}
