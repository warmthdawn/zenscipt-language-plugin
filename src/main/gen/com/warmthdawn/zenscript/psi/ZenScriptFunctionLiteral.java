// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptFunctionLiteral extends ZenScriptLiteralExpression {

  @Nullable
  ZenScriptFunctionBody getFunctionBody();

  @NotNull
  ZenScriptParameters getParameters();

  @Nullable
  ZenScriptType getType();

}
