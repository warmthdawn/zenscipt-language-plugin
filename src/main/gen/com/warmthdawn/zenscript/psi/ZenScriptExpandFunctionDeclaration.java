// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptExpandFunctionDeclaration extends PsiElement {

  @Nullable
  ZenScriptFunctionBody getFunctionBody();

  @Nullable
  ZenScriptIdentifier getIdentifier();

  @Nullable
  ZenScriptParameters getParameters();

  @NotNull
  List<ZenScriptType> getTypeList();

}
