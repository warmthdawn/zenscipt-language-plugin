package com.warmthdawn.zenscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

interface ZenScriptClass : PsiElement, ZenScriptNamedElement {
    val members: List<ZenScriptMember>
    val variables: List<ZenScriptVariableDeclaration>
    val functions: List<ZenScriptFunctionDeclaration>
    val constructors: List<ZenScriptConstructorDeclaration>
}