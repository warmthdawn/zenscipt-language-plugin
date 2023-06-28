package com.warmthdawn.zenscript.reference

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.application.*
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.*
import com.intellij.psi.search.*
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import com.warmthdawn.zenscript.ZSLanguageFileType
import com.warmthdawn.zenscript.psi.ZenScriptClassDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptConstructorDeclaration

private fun SearchScope.restrictByFileType(fileType: FileType): SearchScope = when (this) {
    is GlobalSearchScope -> GlobalSearchScope.getScopeRestrictedByFileTypes(this, fileType)
    is LocalSearchScope -> {
        val elements = scope.filter { it.containingFile.fileType == fileType }
        when (elements.size) {
            0 -> LocalSearchScope.EMPTY
            scope.size -> this
            else -> LocalSearchScope(elements.toTypedArray())
        }
    }

    else -> this
}


class ZenScriptConstructorReferenceSearcher :
    QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>() {
    override fun processQuery(
        queryParameters: ReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference>
    ) {
        val target = queryParameters.elementToSearch
        val onlyZSFiles = runReadAction {
            queryParameters.effectiveSearchScope.restrictByFileType(ZSLanguageFileType)
        }
        if (SearchScope.isEmptyScope(onlyZSFiles)) return

        val (name, constructors) = runReadAction {
            if (!target.isValid) {
                return@runReadAction null
            }
            when (target) {
                is ZenScriptConstructorDeclaration -> {
                    val name = (target.parent as ZenScriptClassDeclaration).name!!
                    name to listOf(target)
                }

                is ZenScriptClassDeclaration -> {
                    target.name!! to target.constructors
                }

                is PsiClass -> {
                    target.name!! to target.constructors.toList()
                }

                else -> null
            }
        } ?: return

        constructors.forEach {
            queryParameters.optimizer.searchWord(
                name, onlyZSFiles, UsageSearchContext.IN_CODE, true, it
            )
        }
    }
}

class ZenScriptPropertyAccessReferenceSearcher :
    QueryExecutorBase<PsiReference, MethodReferencesSearch.SearchParameters>() {
    override fun processQuery(
        queryParameters: MethodReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference>
    ) {
        val method = queryParameters.method
        val onlyZSFiles = runReadAction {
            queryParameters.effectiveSearchScope.restrictByFileType(ZSLanguageFileType)
        }
        if (SearchScope.isEmptyScope(onlyZSFiles)) return
        val propertyNames = getReferenceSearchNames(method)
        for (propertyName in propertyNames) {
            queryParameters.optimizer.searchWord(
                propertyName, onlyZSFiles, UsageSearchContext.IN_CODE, true, method
            )
        }
    }


    private fun getReferenceSearchNames(element: PsiElement) = runReadAction {
        val result = mutableSetOf<String>()
        if (element is PsiMethod) {
            element.getAnnotation("stanhebben.zenscript.annotations.ZenMethod")?.let {
                AnnotationUtil.getStringAttributeValue(it, "value")
            }?.let {
                result.add(it)
            }

            element.getAnnotation("stanhebben.zenscript.annotations.ZenGetter")?.let {
                AnnotationUtil.getStringAttributeValue(it, "value")
            }?.let {
                result.add(it)
            }

            element.getAnnotation("stanhebben.zenscript.annotations.ZenSetter")?.let {
                AnnotationUtil.getStringAttributeValue(it, "value")
            }?.let {
                result.add(it)
            }

        }
        result
    }


}
