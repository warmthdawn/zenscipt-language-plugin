// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptForeachStatement extends PsiElement {

  @Nullable
  ZenScriptExpression getExpression();

  @Nullable
  ZenScriptForeachBody getForeachBody();

  @NotNull
  List<ZenScriptIdentifier> getIdentifierList();

}
