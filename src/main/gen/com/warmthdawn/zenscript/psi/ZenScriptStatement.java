// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptStatement extends PsiElement {

  @Nullable
  ZenScriptBlockStatement getBlockStatement();

  @Nullable
  ZenScriptBreakStatement getBreakStatement();

  @Nullable
  ZenScriptContinueStatement getContinueStatement();

  @Nullable
  ZenScriptExpressionStatement getExpressionStatement();

  @Nullable
  ZenScriptForeachStatement getForeachStatement();

  @Nullable
  ZenScriptIfStatement getIfStatement();

  @Nullable
  ZenScriptReturnStatement getReturnStatement();

  @Nullable
  ZenScriptVariableDeclaration getVariableDeclaration();

  @Nullable
  ZenScriptWhileStatement getWhileStatement();

}
