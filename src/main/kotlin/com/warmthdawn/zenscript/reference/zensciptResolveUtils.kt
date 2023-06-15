package com.warmthdawn.zenscript.reference

import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.index.ZenScriptGlobalVariableIndex
import com.warmthdawn.zenscript.psi.ZenScriptArrayIndexExpression
import com.warmthdawn.zenscript.psi.ZenScriptCallExpression
import com.warmthdawn.zenscript.psi.ZenScriptClassDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptClassTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptFile
import com.warmthdawn.zenscript.psi.ZenScriptFunctionDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptImportDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptLocalAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptMemberAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptNamedElement
import com.warmthdawn.zenscript.psi.ZenScriptReference
import com.warmthdawn.zenscript.psi.ZenScriptScriptBody
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.type.*

fun resolveZenScriptReference(ref: ZenScriptReference, incompleteCode: Boolean): Array<out ResolveResult> {

    return when (ref) {
        is ZenScriptLocalAccessExpression -> resolveLocalAccessExpr(ref)
        is ZenScriptMemberAccessExpression -> resolveMemberAccessExpr(ref)
        is ZenScriptCallExpression -> resolveCallExpr(ref)
        is ZenScriptArrayIndexExpression -> resolveArrayIndexExpr(ref)
        is ZenScriptClassTypeRef -> resolveClassTypeRef(ref)
        is ZenScriptImportDeclaration -> resolveImportDecl(ref)
        else -> emptyArray()
    }
}

fun resolveArrayIndexExpr(ref: ZenScriptArrayIndexExpression): Array<out ResolveResult> {
    return emptyArray()
}

fun resolveImportDecl(ref: ZenScriptImportDeclaration): Array<ZenScriptElementResolveResult> {
    val qualifiedNameEl = ref.qualifiedName ?: return emptyArray()
    val qualifier = qualifiedNameEl.qualifier?.text
    val identifier = qualifiedNameEl.identifier!!.text
    val project = ref.project

    val qualifiedName = if (qualifier != null) "$qualifier.$identifier" else identifier

    if (qualifier != null && qualifier.startsWith("scripts")) {
        val member = findZenFileMember(project, qualifier, identifier)
        if (member != null) {
            val type = if (member is ZenScriptClassDeclaration) ZenResolveResultType.ZEN_CLASS else ZenResolveResultType.ZEN_VARIABLE
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

fun resolveClassTypeRef(ref: ZenScriptClassTypeRef): Array<ResolveResult> {
    val name = ref.qualifiedName.text
    var out: ZenScriptNamedElement? = null
    val notFound = PsiTreeUtil.treeWalkUp(ZenScriptScopeProcessor { element, parent, _ ->
        if ((element is ZenScriptClassDeclaration || element is ZenScriptImportDeclaration) && (element as ZenScriptNamedElement).name == name) {
            out = element
            false
        } else {
            true
        }
    }, ref, null, ResolveState.initial())
    val found = out
    if (found is ZenScriptImportDeclaration) {
        val target = found.multiResolve(true)

        if (target.isEmpty()) {
            return emptyArray()
        }
        val type = (target[0] as? ZenScriptElementResolveResult)?.type
        if (type != ZenResolveResultType.ZEN_CLASS && type != ZenResolveResultType.JAVA_CLASS) {
            return emptyArray()
        }

        return target
    }

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


    return arrayOf(ZenScriptElementResolveResult(found!!, ZenResolveResultType.ZEN_CLASS))
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

fun resolveMemberAccessExpr(ref: ZenScriptMemberAccessExpression): Array<out ResolveResult> {
    val identifier = ref.identifier
    val project = ref.project
    if (identifier == null) {
        return emptyArray()
    }
    val name = identifier.text

    val qualifierExpr = ref.expression
    var qualifierType: ZenType? = null
    if (qualifierExpr is ZenScriptReference) {
        var qualifierTarget = qualifierExpr.resolve()
        if (qualifierTarget is ZenScriptImportDeclaration) {
            qualifierTarget = qualifierTarget.resolve()
        }
        if (qualifierTarget is ZenScriptClassDeclaration || qualifierTarget is PsiClass) {
            // static access
            return findStaticMembers(project, qualifierTarget, name)
        }
        if (qualifierTarget != null) {
            qualifierType = getTargetType(qualifierTarget)
        }
    }

    if (qualifierType == null) {
        qualifierType = getType(qualifierExpr)
    }

    return findMembers(project, qualifierType, name).toTypedArray()
}


private fun resolveLocalAccessExpr(ref: ZenScriptLocalAccessExpression): Array<ResolveResult> {
    val identifier = ref.identifier
    val project = ref.project
    if (identifier != null) {
        val name = identifier.text
        var out: ZenScriptNamedElement? = null
        val processor = ZenScriptScopeProcessor { element, parent, _ ->
            if (element is ZenScriptNamedElement && element.name == name) {
                out = element
                false
            } else {
                true
            }
        }
        val found = out
        val notFound = PsiTreeUtil.treeWalkUp(processor, ref, null, ResolveState.initial())
        return if (!notFound) {

            val type = when (found) {
                is ZenScriptClassDeclaration -> ZenResolveResultType.ZEN_CLASS
                is ZenScriptFunctionDeclaration -> ZenResolveResultType.ZEN_METHOD
                is ZenScriptImportDeclaration -> ZenResolveResultType.ZEN_IMPORT
                else -> ZenResolveResultType.ZEN_VARIABLE
            }

            arrayOf(ZenScriptElementResolveResult(found!!, type))
        } else {
            val result = mutableListOf<PsiNameIdentifierOwner>()
            FileBasedIndex.getInstance().getFilesWithKey(ZenScriptGlobalVariableIndex.NAME, setOf(name), {
                PsiManager.getInstance(project).findFile(it)?.let { psiFile ->
                    collectGlobalFromFile(name, psiFile, result)
                }
                true
            }, GlobalSearchScope.projectScope(project))

            var type = ZenResolveResultType.ZEN_VARIABLE
            if (result.isEmpty()) {
                type = ZenResolveResultType.JAVA_GLOBAL_VAR
                collectLibraryGlobals(name, result)
            }

            result.asSequence()
                    .map { ZenScriptElementResolveResult(it, type) }
                    .toList()
                    .toTypedArray()
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

