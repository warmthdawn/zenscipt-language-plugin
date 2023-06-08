// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

public interface ZenScriptForeachStatement extends ZenScriptStatement, ZenScriptLoopStatement {

  @NotNull
  List<ZenScriptForeachVariableDeclaration> getEntries();

  @Nullable
  ZenScriptExpression getIterTarget();

  @Nullable
  ZenScriptStatement getBody();

  boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place);

}
