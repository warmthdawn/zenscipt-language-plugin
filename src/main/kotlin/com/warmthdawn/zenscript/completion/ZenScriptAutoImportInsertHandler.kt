package com.warmthdawn.zenscript.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import org.jetbrains.kotlin.idea.base.analysis.withRootPrefixIfNeeded
import org.jetbrains.kotlin.renderer.render

class ZenScriptAutoImportInsertHandler(val other: InsertHandler<LookupElement>, val importedName: String) :
    InsertHandler<LookupElement> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {


        other.handleInsert(context, item)


    }
}


class ZenScriptShortenFQInsertHandler(private val fullQualifiedName: String) : InsertHandler<LookupElement> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        context.document.replaceString(
            context.startOffset,
            context.tailOffset,
            fullQualifiedName
        )

        context.commitDocument()
    }
}