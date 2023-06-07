// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptFunctionDeclaration extends ZenScriptNamedElement, ZenScriptFunction {

  @Nullable
  ZenScriptFunctionBody getFunctionBody();

  @Nullable
  ZenScriptIdentifier getIdentifier();

  @Nullable
  ZenScriptParameters getParameters();

  @Nullable
  ZenScriptType getReturnType();

}
