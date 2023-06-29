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


                    if (shouldSuppressCompletion(parameters, result.prefixMatcher)) {
                        result.stopHere()
                        return
                    }
                    ZenScriptKeywordCompletion(element, result).addAllKeywords()
                    ZenScriptMemberCompletion(
                        element,
                        result,
                        parameters.offset,
                        !parameters.isCompleteOnlyNotImported
                    ).addAllMembers()
                }
            }
        )


//        extend(
//                CompletionType.SMART,
//                psiElement().withLanguage(ZSLanguage),
//                object : CompletionProvider<CompletionParameters>() {
//                    override fun addCompletions(
//                            parameters: CompletionParameters,
//                            context: ProcessingContext,
//                            result: CompletionResultSet
//                    ) {
//                        val file = parameters.originalFile
//                        val element = parameters.position
//
//                        ZenScriptMemberCompletion(element, result).addAllMembers()
//                    }
//                }
//        )
    }

    private fun shouldSuppressCompletion(parameters: CompletionParameters, prefixMatcher: PrefixMatcher): Boolean {
        val position = parameters.position
        val invocationCount = parameters.invocationCount

        // no completion inside number literals
        if (ZenScriptPatterns.AFTER_NUMBER_LITERAL.accepts(position)) {
            return true
        }

        return invocationCount == 0 && prefixMatcher.prefix.isEmpty() && ZenScriptPatterns.AFTER_INTEGER_LITERAL_AND_DOT.accepts(
            position
        )
    }

}