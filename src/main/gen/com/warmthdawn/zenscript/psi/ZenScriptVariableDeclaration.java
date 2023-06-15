// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ZenScriptVariableDeclaration extends ZenScriptStatement, ZenScriptMember, ZenScriptNamedElement {

  @Nullable
  ZenScriptIdentifier getIdentifier();

  @Nullable
  ZenScriptTypeRef getTypeRef();

  @Nullable
  ZenScriptInitializerOrDefault getInitializer();

}
