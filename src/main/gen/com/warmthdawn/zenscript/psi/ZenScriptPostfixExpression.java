// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptPostfixExpression extends PsiElement {

  @NotNull
  List<ZenScriptArguments> getArgumentsList();

  @NotNull
  List<ZenScriptExpression> getExpressionList();

  @NotNull
  List<ZenScriptIdentifier> getIdentifierList();

  @NotNull
  ZenScriptPrimaryExpression getPrimaryExpression();

  @NotNull
  List<ZenScriptTypeLiteral> getTypeLiteralList();

}
