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

public class ZenScriptPrimaryExpressionImpl extends ZenScriptExpressionImpl implements ZenScriptPrimaryExpression {

  public ZenScriptPrimaryExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ZenScriptVisitor visitor) {
    visitor.visitPrimaryExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ZenScriptVisitor) accept((ZenScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ZenScriptBracketHandler getBracketHandler() {
    return findChildByClass(ZenScriptBracketHandler.class);
  }

  @Override
  @NotNull
  public List<ZenScriptExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptExpression.class);
  }

  @Override
  @Nullable
  public ZenScriptFunctionBody getFunctionBody() {
    return findChildByClass(ZenScriptFunctionBody.class);
  }

  @Override
  @Nullable
  public ZenScriptIdentifier getIdentifier() {
    return findChildByClass(ZenScriptIdentifier.class);
  }

  @Override
  @NotNull
  public List<ZenScriptMapEntry> getMapEntryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptMapEntry.class);
  }

  @Override
  @NotNull
  public List<ZenScriptParameter> getParameterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ZenScriptParameter.class);
  }

  @Override
  @Nullable
  public ZenScriptTypeLiteral getTypeLiteral() {
    return findChildByClass(ZenScriptTypeLiteral.class);
  }

  @Override
  @Nullable
  public PsiElement getIntLiteral() {
    return findChildByType(INT_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getStringLiteral() {
    return findChildByType(STRING_LITERAL);
  }

}
