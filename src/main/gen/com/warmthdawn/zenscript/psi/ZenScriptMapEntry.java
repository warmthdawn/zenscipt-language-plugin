// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptMapEntry extends PsiElement {

  @NotNull
  List<ZenScriptExpression> getExpressionList();

  @NotNull
  ZenScriptExpression getKey();

  @Nullable
  ZenScriptExpression getValue();

}
