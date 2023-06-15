// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptFunctionTypeRef extends ZenScriptTypeRef {

  @NotNull
  List<ZenScriptTypeRef> getTypeRefList();

  @Nullable
  ZenScriptTypeRef getReturnType();

  @NotNull
  List<ZenScriptTypeRef> getParamsType();

}
