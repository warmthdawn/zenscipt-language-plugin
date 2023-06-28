package com.warmthdawn.zenscript.reference

import com.intellij.find.impl.HelpID
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.*
import com.intellij.util.indexing.IndexingBundle
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.util.hasGlobalModifier
import com.warmthdawn.zenscript.util.hasStaticModifier

class ZenScriptUsageProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return ZenScriptWordsScanner()
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is ZenScriptClassDeclaration ||
                psiElement is ZenScriptFunctionDeclaration ||
                psiElement is ZenScriptExpandFunctionDeclaration ||
                psiElement is ZenScriptConstructorDeclaration ||
                psiElement is ZenScriptVariableDeclaration ||
                psiElement is ZenScriptForeachVariableDeclaration ||
                psiElement is ZenScriptParameter ||
                psiElement is ZenScriptFile
    }

    override fun getHelpId(psiElement: PsiElement): String {
        return when (psiElement) {
            is ZenScriptClassDeclaration -> HelpID.FIND_CLASS_USAGES
            is ZenScriptFunctionDeclaration, is ZenScriptExpandFunctionDeclaration -> HelpID.FIND_METHOD_USAGES
            else -> com.intellij.lang.HelpID.FIND_OTHER_USAGES
        }
    }

    override fun getType(element: PsiElement): String {
        return when (element) {
            // TODO bundle mesesage
            is ZenScriptFunctionDeclaration -> "function"
            is ZenScriptClassDeclaration -> "class"
            is ZenScriptParameter -> "parameter"
            is ZenScriptVariableDeclaration -> {
                val parent = element.parent
                if (parent is ZenScriptClassDeclaration) {
                    "field"
                } else if (parent is ZenScriptScriptBody && (element.hasGlobalModifier || element.hasStaticModifier)) {
                    "property"
                } else {
                    "variable"
                }
            }

            is ZenScriptForeachVariableDeclaration -> "variable"
            is ZenScriptConstructorDeclaration -> "constructor"
            is ZenScriptExpandFunctionDeclaration -> "expand function"
            is ZenScriptFile -> IndexingBundle.message("terms.file")
            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when (element) {
            is ZenScriptFunctionDeclaration -> "${element.name}(...)"
            is ZenScriptClassDeclaration -> element.name ?: ""
            is ZenScriptParameter -> element.name ?: ""
            is ZenScriptConstructorDeclaration -> "${(element.parent as ZenScriptClassDeclaration).name}(...)"
            is ZenScriptExpandFunctionDeclaration -> "${element.name}(...)"
            is ZenScriptFile -> element.name
            is PsiNamedElement -> element.name ?: ""
            else -> ""
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return getDescriptiveName(element)
    }
}