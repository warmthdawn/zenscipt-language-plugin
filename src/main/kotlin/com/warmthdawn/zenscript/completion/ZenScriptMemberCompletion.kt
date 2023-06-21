package com.warmthdawn.zenscript.completion;

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.completion.ZenScriptPatterns.AT_TOP_LEVEL
import com.warmthdawn.zenscript.index.ZenScriptClassNameIndex
import com.warmthdawn.zenscript.index.ZenScriptMemberCache
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.reference.ZenResolveResultType
import com.warmthdawn.zenscript.type.*

class ZenScriptMemberCompletion(
        private val position: PsiElement,
        private val result: CompletionResultSet,
        private val addAutoImport: Boolean,
) {
    companion object {
    }

    val isTopLevel = AT_TOP_LEVEL.accepts(position)
    val project = position.project
    fun addAllMembers() {


        val directOwner = position.parent?.parent

        if (directOwner is ZenScriptMemberAccessExpression) {
            completeMemberAccessExpr(directOwner)
            return
        }

        if (directOwner is ZenScriptLocalAccessExpression) {
            completeLocalAccessExpr(directOwner)
            return
        }

        when (val element = directOwner?.parent) {
            is ZenScriptImportReference -> {
                completeImportRef(element)
            }
            is ZenScriptClassTypeRef -> {
                completeTypeRef(element)
            }

        }


    }

    private fun addElementCandidates(candidates: List<Pair<String, PsiElement>>) {

    }

    private fun addClassCandidate(name: String, qualifiedName: String) {
//        name.
        if(qualifiedName.startsWith("scripts")) {
            findZenClass(project, qualifiedName)
        } else {
            findJavaClass(project, qualifiedName)
        }
    }
    private fun addPackageCandidate(name: String, qualifiedName: String) {

    }


    private fun completeMemberAccessExpr(element: ZenScriptMemberAccessExpression) {
        val project = element.project
        val qualifierExpr = element.expression
        var qualifierType: ZenType? = null
        if (qualifierExpr is ZenScriptReference) {
            val qualifierTarget = qualifierExpr.advancedResolve()
            if (qualifierTarget.isNotEmpty()) {
                val firstType = qualifierTarget[0].type

                if (firstType == ZenResolveResultType.ZEN_CLASS || firstType == ZenResolveResultType.JAVA_CLASS) {
                    // static access

                    val candidates = findStaticMembers(project, qualifierTarget[0].element)
                    addElementCandidates(candidates)
                    return
                }
                qualifierType = getTargetType(qualifierTarget, false)
            }
        }

        if (qualifierType == null) {
            qualifierType = getType(qualifierExpr)
        }

        if(qualifierType is ZenScriptPackageType) {
            // addPackages
            val packageName = "${qualifierType.packageName}."
            val packageNameLen = packageName.length
            val packageNames = mutableSetOf<String>()
            for (className in FileBasedIndex.getInstance().getAllKeys(ZenScriptClassNameIndex.NAME, project)) {
                if (className.startsWith(packageName)) {
                    break
                }
                val last = className.substring(packageNameLen)
                val dotIndex = last.indexOf('.')
                if(dotIndex > 0) {
                    val name = last.substring(0, dotIndex)
                    if(name !in packageNames) {
                        packageNames.add(name)
                        addPackageCandidate(name, packageName + name)
                    }

                } else {
                    addClassCandidate(last, packageName + last)
                }
            }
        }

        val candidates = findMembers(project, qualifierType)
        addElementCandidates(candidates)

    }

    private fun completeTypeRef(element: ZenScriptClassTypeRef) {

    }

    private fun completeImportRef(element: ZenScriptImportReference) {

    }

    private fun completeLocalAccessExpr(element: ZenScriptLocalAccessExpression) {

    }

}