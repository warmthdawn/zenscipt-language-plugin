// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public interface ZenScriptBinaryExpression extends ZenScriptExpression {

  @NotNull
  List<ZenScriptExpression> getExpressionList();

  @NotNull
  ZenScriptExpression getLeft();

  @Nullable
  ZenScriptExpression getRight();

  @NotNull
  IElementType getOperator();

}
