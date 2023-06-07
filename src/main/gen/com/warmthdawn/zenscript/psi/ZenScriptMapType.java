// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptMapType extends ZenScriptType {

  @NotNull
  List<ZenScriptType> getTypeList();

  @Nullable
  ZenScriptType getKeyType();

  @NotNull
  ZenScriptType getValueType();

}
