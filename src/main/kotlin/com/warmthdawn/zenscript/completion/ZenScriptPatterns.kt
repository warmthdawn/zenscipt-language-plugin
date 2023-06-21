package com.warmthdawn.zenscript.completion

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ObjectPattern
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.StandardPatterns.*
import com.warmthdawn.zenscript.psi.ZenScriptClass
import com.warmthdawn.zenscript.psi.ZenScriptClassDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptExpandFunctionDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptFunctionDeclaration

object ZenScriptPatterns {


    val AT_TOP_LEVEL = not(
            psiElement().inside(
                    or(
                            psiElement(ZenScriptClassDeclaration::class.java),
                            psiElement(ZenScriptFunctionDeclaration::class.java),
                            psiElement(ZenScriptExpandFunctionDeclaration::class.java)
                    )
            )
    )

    fun zsClass(): PsiElementPattern.Capture<ZenScriptClass> {
        return psiElement(ZenScriptClass::class.java)
    }
}

