// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptClassDeclaration extends PsiElement {

  @NotNull
  List<ZenScriptConstructorDeclaration> getConstructorDeclarationList();

  @NotNull
  List<ZenScriptFunctionDeclaration> getFunctionDeclarationList();

  @NotNull
  ZenScriptQualifiedName getQualifiedName();

  @NotNull
  List<ZenScriptVariableDeclaration> getVariableDeclarationList();

}
