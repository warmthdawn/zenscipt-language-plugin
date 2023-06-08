// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptScriptBody extends ZenScriptCodeBlock {

  @NotNull
  List<ZenScriptStatement> getStatements();

  @NotNull
  List<ZenScriptFunctionDeclaration> getFunctions();

  @NotNull
  List<ZenScriptClassDeclaration> getClasses();

  @NotNull
  List<ZenScriptExpandFunctionDeclaration> getExpandFunctions();

}
