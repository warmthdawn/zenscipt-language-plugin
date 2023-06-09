// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

public interface ZenScriptFunctionLiteral extends ZenScriptLiteralExpression, ZenScriptFunction {

  @Nullable
  ZenScriptFunctionBody getFunctionBody();

  @Nullable
  ZenScriptParameters getParameters();

  @Nullable
  ZenScriptTypeRef getReturnTypeRef();

  boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place);

}
