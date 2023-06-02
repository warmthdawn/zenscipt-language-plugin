// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptScriptBody extends PsiElement {

  @NotNull
  List<ZenScriptClassDeclaration> getClassDeclarationList();

  @NotNull
  List<ZenScriptExpandFunctionDeclaration> getExpandFunctionDeclarationList();

  @NotNull
  List<ZenScriptFunctionDeclaration> getFunctionDeclarationList();

  @NotNull
  List<ZenScriptStatement> getStatementList();

}
