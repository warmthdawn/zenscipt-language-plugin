// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptImportDeclaration extends ZenScriptNamedElement {

  @Nullable
  ZenScriptImportReference getImportReference();

  @Nullable
  ZenScriptIdentifier getIdentifier();

  @NotNull
  ZenScriptImportDeclaration setName(@NotNull String name);

  @Nullable
  ZenScriptIdentifier getAlias();

}
