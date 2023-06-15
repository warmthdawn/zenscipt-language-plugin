package com.warmthdawn.zenscript.psi

import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiNamedElement
import com.warmthdawn.zenscript.util.createIdentifierFromText

interface ZenScriptNamedElement: PsiElement, PsiNamedElement, NavigationItem, PsiNameIdentifierOwner {
    val identifier: ZenScriptIdentifier?



}