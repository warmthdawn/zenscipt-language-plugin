package com.warmthdawn.zenscript.completion

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ObjectPattern
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.StandardPatterns.*
import com.warmthdawn.zenscript.psi.ZenScriptClass

object ZenScriptPatterns{

    inline fun <reified T>instanceOf(): ObjectPattern.Capture<T> {
        return instanceOf(T::class.java)
    }


    fun zsClass(): PsiElementPattern.Capture<ZenScriptClass> {
        return psiElement(ZenScriptClass::class.java)
    }
}

