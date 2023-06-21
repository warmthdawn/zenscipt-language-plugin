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
import com.warmthdawn.zenscript.psi.ZenScriptImportReference
import com.warmthdawn.zenscript.psi.ZenScriptLocalAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptMemberAccessExpression
import com.warmthdawn.zenscript.psi.ZenScriptNamedElement
import com.warmthdawn.zenscript.psi.ZenScriptReference
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.type.*

fun resolveZenScriptReference(ref: ZenScriptReference, incompleteCode: Boolean): Array<ZenScriptElementResolveResult> {
    val results = when (ref) {
        is ZenScriptLocalAccessExpression -> resolveLocalAccessExpr(ref)
        is ZenScriptMemberAccessExpression -> resolveMemberAccessExpr(ref)
        is ZenScriptClassTypeRef -> resolveClassTypeRef(ref)
        is ZenScriptImportReference -> resolveImportRef(ref)
        else -> emptyArray()
    }
    val parent = ref.parent
    if (parent is ZenScriptCallExpression) {
        return resolveCallExpr(parent, results)
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

fun resolveClassTypeRef(ref: ZenScriptClassTypeRef): Array<ZenScriptElementResolveResult> {
    val name = ref.qualifiedName!!.text
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
        val target = found.importReference!!.advancedResolve(true)

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

fun resolveCallExpr(ref: ZenScriptCallExpression, el: Array<ZenScriptElementResolveResult>): Array<ZenScriptElementResolveResult> {
    val project = ref.project
    val typeUtil = ZenScriptTypeService.getInstance(project)
    val arguments = ref.arguments.expressionList.map { getType(it) }

    var resolvedMethods = el


    if (resolvedMethods.isNotEmpty()) {
        if (resolvedMethods[0].type == ZenResolveResultType.ZEN_IMPORT) {
            resolvedMethods = (resolvedMethods[0].element as ZenScriptImportDeclaration).importReference!!.advancedResolve(false)
        }
    }

    if (resolvedMethods.isEmpty()) {
        return emptyArray()
    }

    val candidateMethods = resolvedMethods.asSequence()
            .filter { it.type == ZenResolveResultType.ZEN_METHOD || it.type == ZenResolveResultType.JAVA_METHODS }
            .map { it.element }
            .toList()
    if (candidateMethods.isNotEmpty()) {

        val selected = typeUtil.selectMethod(arguments, candidateMethods)

        if (selected == -2) {
            return candidateMethods.map {
                val type = if (it is PsiMethod) ZenResolveResultType.JAVA_METHODS else ZenResolveResultType.ZEN_METHOD
                ZenScriptElementResolveResult(it, type, false)
            }.toTypedArray()
        }
        if (selected < 0) {
            return emptyArray()
        }
        val method = candidateMethods[selected]
        val type = if (method is PsiMethod) ZenResolveResultType.JAVA_METHODS else ZenResolveResultType.ZEN_METHOD

        return arrayOf(ZenScriptElementResolveResult(method, type))

    }

    if (resolvedMethods.isNotEmpty()) {
        when (resolvedMethods[0].type) {
            ZenResolveResultType.ZEN_CLASS, ZenResolveResultType.JAVA_CLASS -> {
                val clazz = resolvedMethods[0].element
                val ctors = if (clazz is ZenScriptClassDeclaration) clazz.constructorDeclarationList
                else listOf(*(clazz as PsiClass).constructors)
                val selected = typeUtil.selectMethod(arguments, ctors)
                if (selected == -2) {
                    return ctors.map { ZenScriptElementResolveResult(it, if (it is PsiMethod) ZenResolveResultType.JAVA_METHODS else ZenResolveResultType.ZEN_METHOD, false) }.toTypedArray()
                } else if (selected < 0) {
                    return emptyArray()
                }
                val method = ctors[selected]
                val type = if (method is PsiMethod) ZenResolveResultType.JAVA_METHODS else ZenResolveResultType.ZEN_METHOD
                return arrayOf(ZenScriptElementResolveResult(method, type))
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
        var qualifierTarget = qualifierExpr.advancedResolve()
        if (qualifierTarget.isNotEmpty()) {
            val firstType = qualifierTarget[0].type

            if (firstType == ZenResolveResultType.ZEN_IMPORT) {
                qualifierTarget = (qualifierTarget[0].element as ZenScriptImportDeclaration).importReference!!.advancedResolve()
            }
        }
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
        var out: ZenScriptNamedElement? = null
        val processor = ZenScriptScopeProcessor { element, parent, _ ->
            if (element is ZenScriptNamedElement && element.name == name) {
                out = element
                false
            } else {
                true
            }
        }
        val notFound = PsiTreeUtil.treeWalkUp(processor, ref, null, ResolveState.initial())
        val found = out
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

