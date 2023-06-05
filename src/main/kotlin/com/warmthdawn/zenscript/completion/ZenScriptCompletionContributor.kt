package com.warmthdawn.zenscript.completion

import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.util.ProcessingContext
import com.warmthdawn.zenscript.ZSLanguage


class ZenScriptCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().withLanguage(ZSLanguage),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val file = parameters.originalFile
                    val element = parameters.position

                    ZenScriptKeywordCompletion(element, result).addAllKeywords()
                }
            }
        )
    }

}