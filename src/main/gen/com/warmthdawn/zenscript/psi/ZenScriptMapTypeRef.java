// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptMapTypeRef extends ZenScriptTypeRef {

  @NotNull
  List<ZenScriptTypeRef> getTypeRefList();

  @Nullable
  ZenScriptTypeRef getKeyType();

  @NotNull
  ZenScriptTypeRef getValueType();

}
