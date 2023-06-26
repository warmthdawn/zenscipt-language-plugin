package com.warmthdawn.zenscript.reference

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.external.ZenScriptGlobalData
import com.warmthdawn.zenscript.index.ZenScriptGlobalVariableIndex
import com.warmthdawn.zenscript.psi.ZenScriptCallExpression
import com.warmthdawn.zenscript.psi.ZenScriptClassDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptClassTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptFile
import com.warmthdawn.zenscript.psi.ZenScriptFunctionDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptImportDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptImportReference
import com.warmthdawn.zenscript.psi.ZenScriptLocalAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptMemberAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptNamedElement
import com.warmthdawn.zenscript.psi.ZenScriptReference
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.type.*

fun resolveZenScriptReference(
    ref: ZenScriptReference,
    incompleteCode: Boolean,
    filterMethods: Boolean = true
): Array<ZenScriptElementResolveResult> {
    val results = when (ref) {
        is ZenScriptLocalAccessExpression -> resolveLocalAccessExpr(ref)
        is ZenScriptMemberAccessExpression -> resolveMemberAccessExpr(ref)
        is ZenScriptClassTypeRef -> resolveClassTypeRef(ref)
        is ZenScriptImportReference -> resolveImportRef(ref)
        else -> emptyArray()
    }
    if (filterMethods) {
        val parent = ref.parent
        if (parent is ZenScriptCallExpression) {
            return filterCallExpr(parent, results)
        }
    }
    return results
}

fun resolveImportRef(ref: ZenScriptImportReference): Array<ZenScriptElementResolveResult> {
    val qualifiedNameEl = ref.qualifiedName ?: return emptyArray()
    val qualifier = qualifiedNameEl.qualifier?.text?.let { it.substring(0, it.length - 1) }
    val identifier = qualifiedNameEl.identifier!!.text
    val project = ref.project

    val qualifiedName = if (qualifier != null) "$qualifier.$identifier" else identifier

    if (qualifier != null && qualifier.startsWith("scripts")) {
        val member = findZenFileMember(project, qualifier, identifier)
        if (member != null) {
            val type =
                if (member is ZenScriptClassDeclaration) ZenResolveResultType.ZEN_CLASS else ZenResolveResultType.ZEN_VARIABLE
            return arrayOf(ZenScriptElementResolveResult(member, type))
        }
    } else {
        var foundJavaClazz = findJavaClass(project, qualifiedName)
        if (foundJavaClazz != null) {
            return arrayOf(ZenScriptElementResolveResult(foundJavaClazz, ZenResolveResultType.JAVA_CLASS))
        }
        if (qualifier != null) {
            foundJavaClazz = findJavaClass(project, qualifier)
        }
        if (foundJavaClazz != null) {
            return findStaticMembers(project, foundJavaClazz, identifier)
        }
    }
    return emptyArray()
}

fun resolveClassTypeRef(ref: ZenScriptClassTypeRef): Array<ZenScriptElementResolveResult> {
    val name = ref.qualifiedName!!.text
    var found: PsiElement? = null
    val notFound = PsiTreeUtil.treeWalkUp(ZenScriptScopeProcessor { element, parent, _ ->
        if (element is ZenScriptClassDeclaration && element.name == name) {
            found = element
            false
        } else if (element is ZenScriptImportDeclaration && element.name == name) {
            found = element.importReference?.resolve()
            false
        } else {
            true
        }
    }, ref, null, ResolveState.initial())
    if (notFound) {

        val qualifiedName = ref.text
        val project = ref.project
        if (qualifiedName.startsWith("scripts")) {
            val zenClass = findZenClass(project, qualifiedName)
            if (zenClass != null) {
                return arrayOf(ZenScriptElementResolveResult(zenClass, ZenResolveResultType.ZEN_CLASS))
            }


        } else {
            val javaClass = findJavaClass(project, qualifiedName)
            if (javaClass != null) {
                return arrayOf(ZenScriptElementResolveResult(javaClass, ZenResolveResultType.JAVA_CLASS))
            }
        }


        return emptyArray()
    }

    return if (found is ZenScriptClassDeclaration) {
        arrayOf(ZenScriptElementResolveResult(found!!, ZenResolveResultType.ZEN_CLASS))
    } else if (found is PsiClass) {
        arrayOf(ZenScriptElementResolveResult(found!!, ZenResolveResultType.JAVA_CLASS))
    } else {
        emptyArray()
    }
}

fun filterCallExpr(
    ref: ZenScriptCallExpression,
    resolvedMethods: Array<ZenScriptElementResolveResult>
): Array<ZenScriptElementResolveResult> {
    val project = ref.project
    val typeUtil = ZenScriptTypeService.getInstance(project)
    val arguments = ref.arguments.expressionList.map { getType(it) }
    if (resolvedMethods.isEmpty()) {
        return emptyArray()
    }

    val candidateMethods = resolvedMethods.asSequence()
        .filter { it.type == ZenResolveResultType.ZEN_METHOD || it.type == ZenResolveResultType.JAVA_METHODS }
        .map { it.element }
        .toList()
    if (candidateMethods.isNotEmpty()) {

        val (priority, result) = typeUtil.selectMethod(arguments, candidateMethods)

        if (priority == ZenCallPriority.INVALID) {
            return candidateMethods.map {
                val type = if (it is PsiMethod) ZenResolveResultType.JAVA_METHODS else ZenResolveResultType.ZEN_METHOD
                ZenScriptElementResolveResult(it, type)
            }.toTypedArray()
        }
        val resultSet = result.toSet()
        return candidateMethods.filterIndexed { index, _ ->
            index in resultSet
        }.map {
            val type = if (it is PsiMethod) ZenResolveResultType.JAVA_METHODS else ZenResolveResultType.ZEN_METHOD
            ZenScriptElementResolveResult(it, type)
        }.toTypedArray()

    }

    if (resolvedMethods.isNotEmpty()) {
        when (resolvedMethods[0].type) {
            ZenResolveResultType.ZEN_CLASS, ZenResolveResultType.JAVA_CLASS -> {
                val clazz = resolvedMethods[0].element
                val ctors = if (clazz is ZenScriptClassDeclaration) {
                    clazz.constructors
                } else {
                    listOf(*(clazz as PsiClass).constructors)
                }

                if (ctors.isEmpty()) {
                    return resolvedMethods
                }

                val (priority, result) = typeUtil.selectMethod(arguments, ctors)

                if (priority == ZenCallPriority.INVALID) {
                    return ctors.map {
                        val type =
                            if (it is PsiMethod) ZenResolveResultType.JAVA_METHODS else ZenResolveResultType.ZEN_METHOD
                        ZenScriptElementResolveResult(it, type)
                    }.toTypedArray()
                }
                val resultSet = result.toSet()
                return ctors.filterIndexed { index, _ ->
                    index in resultSet
                }.map {
                    val type =
                        if (it is PsiMethod) ZenResolveResultType.JAVA_METHODS else ZenResolveResultType.ZEN_METHOD
                    ZenScriptElementResolveResult(it, type)
                }.toTypedArray()
            }

            else -> {
                return resolvedMethods
            }
        }
    }




    return emptyArray()
}

fun resolveMemberAccessExpr(ref: ZenScriptMemberAccessExpression): Array<ZenScriptElementResolveResult> {
    val identifier = ref.identifier
    val project = ref.project
    if (identifier == null) {
        return emptyArray()
    }
    val name = identifier.text

    val qualifierExpr = ref.expression
    var qualifierType: ZenType? = null
    if (qualifierExpr is ZenScriptReference) {
        val qualifierTarget = qualifierExpr.advancedResolve()
        if (qualifierTarget.isNotEmpty()) {
            val firstType = qualifierTarget[0].type

            if (firstType == ZenResolveResultType.ZEN_CLASS || firstType == ZenResolveResultType.JAVA_CLASS) {
                // static access
                return findStaticMembers(project, qualifierTarget[0].element, name)
            }
            qualifierType = getTargetType(qualifierTarget, false)
        }
    }

    if (qualifierType == null) {
        qualifierType = getType(qualifierExpr)
    }

    return findMembers(project, qualifierType, name).toTypedArray()
}


private fun resolveLocalAccessExpr(ref: ZenScriptLocalAccessExpression): Array<ZenScriptElementResolveResult> {
    val identifier = ref.identifier
    val project = ref.project
    if (identifier != null) {
        val name = identifier.text
        val foundElements = mutableListOf<PsiElement>()
        var foundImport: ZenScriptImportDeclaration? = null
        val processor = ZenScriptScopeProcessor { element, parent, _ ->
            if (element is ZenScriptNamedElement && element.name == name) {
                foundElements.add(element)
                false
            } else if (element is ZenScriptImportDeclaration && element.name == name) {
                foundImport = element
                false
            } else {
                true
            }
        }
        val notFound = PsiTreeUtil.treeWalkUp(processor, ref, null, ResolveState.initial())
        return if (!notFound) {
            if (foundImport != null) {
                foundImport!!.importReference!!.advancedResolve()
            } else {
                foundElements.map {
                    val type = when (it) {
                        is ZenScriptClassDeclaration -> ZenResolveResultType.ZEN_CLASS
                        is ZenScriptFunctionDeclaration -> ZenResolveResultType.ZEN_METHOD
                        is ZenScriptImportDeclaration -> ZenResolveResultType.ZEN_IMPORT
                        else -> ZenResolveResultType.ZEN_VARIABLE
                    }
                    ZenScriptElementResolveResult(it, type)
                }.toTypedArray()
            }

        } else {
            val result = mutableListOf<PsiElement>()
            FileBasedIndex.getInstance().getFilesWithKey(ZenScriptGlobalVariableIndex.NAME, setOf(name), {
                PsiManager.getInstance(project).findFile(it)?.let { psiFile ->
                    collectGlobalFromFile(name, psiFile, result)
                }
                true
            }, GlobalSearchScope.projectScope(project))

            var type = ZenResolveResultType.ZEN_VARIABLE
            if (result.isEmpty()) {
                type = ZenResolveResultType.JAVA_GLOBAL_VAR
                result.addAll(collectLibraryGlobalFields(project, name))
            }


            if (result.isEmpty()) {
                type = ZenResolveResultType.JAVA_GLOBAL_FUNCTION
                result.addAll(collectLibraryGlobalMethods(project, name))
            }

            result.asSequence()
                .map { ZenScriptElementResolveResult(it, type) }
                .toList()
                .toTypedArray()
        }
    }

    return emptyArray()

}


private fun collectLibraryGlobalFields(project: Project, name: String): List<PsiElement> {
    return ZenScriptGlobalData.getInstance(project).getGlobalFields()[name] ?: emptyList()
}

private fun collectLibraryGlobalMethods(project: Project, name: String): List<PsiElement> {
    return ZenScriptGlobalData.getInstance(project).getGlobalFunctions()[name] ?: emptyList()
}

private fun collectGlobalFromFile(name: String, file: PsiFile, out: MutableList<PsiElement>) {
    if (file !is ZenScriptFile) {
        return
    }

    (file.scriptBody?.statements ?: return)
        .asSequence()
        .filterIsInstance<ZenScriptVariableDeclaration>()
        .filter { it.name == name }
        .forEach {
            out.add(it)
        }

}

