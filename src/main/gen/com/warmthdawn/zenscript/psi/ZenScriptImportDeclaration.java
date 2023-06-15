// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptImportDeclaration extends ZenScriptReference, ZenScriptNamedElement {

  @Nullable
  ZenScriptQualifiedName getQualifiedName();

  @Nullable
  ZenScriptIdentifier getIdentifier();

  @Nullable
  String getName();

  @NotNull
  ZenScriptImportDeclaration setName(@NotNull String name);

  @Nullable
  PsiElement getNameIdentifier();

  @Nullable
  ZenScriptIdentifier getAlias();

}
