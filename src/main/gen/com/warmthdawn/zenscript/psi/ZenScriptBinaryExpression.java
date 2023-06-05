// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptBinaryExpression extends ZenScriptExpression {

  @NotNull
  List<ZenScriptExpression> getExpressionList();

  @NotNull
  ZenScriptExpression getLeft();

  @Nullable
  ZenScriptExpression getRight();

  //WARNING: getOperator(...) is skipped
  //matching getOperator(ZenScriptBinaryExpression, ...)
  //methods are not found in ZenScriptImplUtil

}
