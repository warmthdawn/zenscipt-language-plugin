// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptFunctionDeclaration extends PsiElement {

  @NotNull
  ZenScriptFunctionBody getFunctionBody();

  @NotNull
  ZenScriptIdentifier getIdentifier();

  @NotNull
  List<ZenScriptParameter> getParameterList();

  @Nullable
  ZenScriptTypeLiteral getTypeLiteral();

}
