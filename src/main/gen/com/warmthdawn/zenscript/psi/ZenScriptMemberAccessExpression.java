// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.openapi.util.TextRange;

public interface ZenScriptMemberAccessExpression extends ZenScriptExpression, ZenScriptReference {

  @NotNull
  ZenScriptExpression getExpression();

  @Nullable
  ZenScriptIdentifier getIdentifier();

  @NotNull
  TextRange getRangeInElement();

}
