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

public class ZenScriptScriptBodyImpl extends ZenScriptCodeBlockImpl implements ZenScriptScriptBody {

  public ZenScriptScriptBodyImpl(ASTNode node) {
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
  public List<ZenScriptStatement> getStatements() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptStatement.class);
  }

  @Override
  @NotNull
  public List<ZenScriptFunctionDeclaration> getFunctions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptFunctionDeclaration.class);
  }

  @Override
  @NotNull
  public List<ZenScriptClassDeclaration> getClasses() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptClassDeclaration.class);
  }

  @Override
  @NotNull
  public List<ZenScriptExpandFunctionDeclaration> getExpandFunctions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptExpandFunctionDeclaration.class);
  }

}
