// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptClassDeclaration extends ZenScriptClass {

  @Nullable
  ZenScriptQualifiedName getQualifiedName();

  @NotNull
  List<ZenScriptVariableDeclaration> getVariables();

  @NotNull
  List<ZenScriptConstructorDeclaration> getConstructors();

  @NotNull
  List<ZenScriptFunctionDeclaration> getFunctions();

}
