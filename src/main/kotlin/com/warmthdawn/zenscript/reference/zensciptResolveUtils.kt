package com.warmthdawn.zenscript.reference

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.toArray
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.index.ZenScriptGlobalVariableIndex
import com.warmthdawn.zenscript.psi.ZenScriptCallExpression
import com.warmthdawn.zenscript.psi.ZenScriptClassDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptClassTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptFile
import com.warmthdawn.zenscript.psi.ZenScriptImportDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptLocalAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptMemberAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptNamedElement
import com.warmthdawn.zenscript.psi.ZenScriptReference
import com.warmthdawn.zenscript.psi.ZenScriptTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.type.*

fun resolveZenScriptReference(ref: ZenScriptReference, incompleteCode: Boolean): Array<ResolveResult> {

    return when (ref) {
        is ZenScriptLocalAccessExpression -> resolveLocalAccessExpr(ref)
        is ZenScriptMemberAccessExpression -> resolveMemberAccessExpr(ref)
        is ZenScriptCallExpression -> resolveCallExpr(ref)
        is ZenScriptClassTypeRef -> resolveClassTypeRef(ref)
        else -> emptyArray()
    }
}

fun resolveClassTypeRef(ref: ZenScriptClassTypeRef): Array<ResolveResult> {
    val name = ref.qualifiedName.text
    var found: ZenScriptNamedElement? = null
    val processor = ZenScriptScopeProcessor { element, parent, _ ->
        if ((element is ZenScriptClassDeclaration || element is ZenScriptImportDeclaration) && (element as ZenScriptNamedElement).name == name) {
            found = element
            false
        } else {
            true
        }
    }
    val notFound = PsiTreeUtil.treeWalkUp(processor, ref, null, ResolveState.initial())
    if(notFound) {

        val qualifiedName = ref.text
        val project = ref.project
        val element = if (qualifiedName.startsWith("scripts")) {
            findZenClass(project, qualifiedName)
        } else {
            findJavaClass(project, qualifiedName)
        }

        if (element != null) {
            return arrayOf(PsiElementResolveResult(element))
        }

        return emptyArray()
    }

    return arrayOf(PsiElementResolveResult(found!!))
}

fun resolveCallExpr(ref: ZenScriptCallExpression): Array<ResolveResult> {
    val callee = ref.expression
    val project = ref.project

    if (callee is ZenScriptMemberAccessExpression) {
        val name = callee.identifier!!.text
        val qualifierType = getType(callee.expression)

        val candidateMethods = findOverridingMethods(project, qualifierType, name)
    }

    return emptyArray()
}

fun resolveMemberAccessExpr(ref: ZenScriptMemberAccessExpression): Array<ResolveResult> {
    val identifier = ref.identifier
    val project = ref.project
    if (identifier == null) {
        return emptyArray()
    }
    val name = identifier.text
    val qualifierType = getType(ref.expression)

    val resolved = findMember(project, qualifierType, name)

    return resolved.map { PsiElementResolveResult(it) }.toTypedArray()

//    return resolved.mapNotNull {
//        when (it) {
//            is ZenScriptNamedElement -> it.identifier
//            is PsiNameIdentifierOwner -> it.identifyingElement
//            else -> null
//        }?.let { e -> PsiElementResolveResult(e) }
//    }.toTypedArray()

}


private fun resolveLocalAccessExpr(ref: ZenScriptLocalAccessExpression): Array<ResolveResult> {
    val identifier = ref.identifier
    val project = ref.project
    if (identifier != null) {
        val name = identifier.text
        var found: ZenScriptNamedElement? = null
        val processor = ZenScriptScopeProcessor { element, parent, _ ->
            if (element is ZenScriptNamedElement && element.name == name) {
                found = element
                false
            } else {
                true
            }
        }
        val notFound = PsiTreeUtil.treeWalkUp(processor, ref, null, ResolveState.initial())
        return if (notFound) {
            val result = mutableListOf<PsiNameIdentifierOwner>()
            FileBasedIndex.getInstance().getFilesWithKey(ZenScriptGlobalVariableIndex.NAME, setOf(name), {
                PsiManager.getInstance(project).findFile(it)?.let { psiFile ->
                    collectGlobalFromFile(name, psiFile, result)
                }
                true
            }, GlobalSearchScope.projectScope(project))

            if (result.isEmpty()) {
                collectLibraryGlobals(name, result)
            }

            result.asSequence()
                    .map { PsiElementResolveResult(it) }
                    .toList()
                    .toTypedArray()
        } else {
            arrayOf(PsiElementResolveResult(found!!))
        }
    }

    return emptyArray()

}


private fun collectLibraryGlobals(name: String, out: MutableList<PsiNameIdentifierOwner>) {

}

private fun collectGlobalFromFile(name: String, file: PsiFile, out: MutableList<PsiNameIdentifierOwner>) {
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

