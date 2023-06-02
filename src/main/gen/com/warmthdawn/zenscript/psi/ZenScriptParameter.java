// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptParameter extends PsiElement {

  @Nullable
  ZenScriptDefaultValue getDefaultValue();

  @NotNull
  ZenScriptIdentifier getIdentifier();

  @Nullable
  ZenScriptTypeLiteral getTypeLiteral();

}
