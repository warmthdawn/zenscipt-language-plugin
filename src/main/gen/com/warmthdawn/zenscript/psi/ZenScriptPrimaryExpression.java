// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptPrimaryExpression extends PsiElement {

  @Nullable
  ZenScriptBracketHandler getBracketHandler();

  @NotNull
  List<ZenScriptExpression> getExpressionList();

  @Nullable
  ZenScriptFunctionBody getFunctionBody();

  @Nullable
  ZenScriptIdentifier getIdentifier();

  @NotNull
  List<ZenScriptMapEntry> getMapEntryList();

  @NotNull
  List<ZenScriptParameter> getParameterList();

  @Nullable
  ZenScriptTypeLiteral getTypeLiteral();

}
