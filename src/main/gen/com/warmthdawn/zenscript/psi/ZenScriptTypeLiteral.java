// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptTypeLiteral extends PsiElement {

  @NotNull
  ZenScriptArraySuffix getArraySuffix();

  @Nullable
  ZenScriptPrimitiveType getPrimitiveType();

  @Nullable
  ZenScriptQualifiedName getQualifiedName();

  @NotNull
  List<ZenScriptTypeLiteral> getTypeLiteralList();

}
