package com.warmthdawn.zenscript.completion;

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PlatformIcons
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.completion.ZenScriptPatterns.AT_TOP_LEVEL
import com.warmthdawn.zenscript.completion.ZenScriptPatterns.ZEN_SCRIPT_IDENTIFIER
import com.warmthdawn.zenscript.external.ZenScriptGlobalData
import com.warmthdawn.zenscript.index.ZenScriptClassNameIndex
import com.warmthdawn.zenscript.index.ZenScriptGlobalVariableIndex
import com.warmthdawn.zenscript.index.ZenScriptScriptFileIndex
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.reference.*
import com.warmthdawn.zenscript.type.*
import com.warmthdawn.zenscript.util.hasGlobalModifier
import com.warmthdawn.zenscript.util.type
import java.lang.IllegalStateException

class ZenScriptMemberCompletion(
    private val position: PsiElement,
    private var result: CompletionResultSet,
    private val offset: Int,
    private val addAutoImport: Boolean,
) {

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

        if (directOwner is ZenScriptQualifiedName) {
            // the default prefix matcher will match the full qualified name
            result = result.withPrefixMatcher(
                CompletionUtil.findIdentifierPrefix(
                    directOwner,
                    offset,
                    ZEN_SCRIPT_IDENTIFIER,
                    ZEN_SCRIPT_IDENTIFIER
                )
            )
            when (val element = directOwner.parent) {
                is ZenScriptImportReference -> {
                    completeImportRef(element)
                }

                is ZenScriptClassTypeRef -> {
                    completeTypeRef(element)
                }

            }
        }


    }


    private fun LookupElementBuilder.add() {
        result.addElement(this@add)
    }

    private fun addJavaClassCandidate(name: String, qualifiedName: String) {
        val javaClass = findJavaClass(project, qualifiedName)
            ?: throw NullPointerException("could not find $qualifiedName")
        LookupElementBuilder
            .create(javaClass, name)
            .withIcon(javaClass.getIcon(0))
            .appendTailText(" (${qualifiedName.substringBeforeLast('.')})", true)
            .add()

    }

    private fun addPackageCandidate(name: String, qualifiedName: String) {

        LookupElementBuilder.create(name)
            .withTailText(" ($qualifiedName)", true)
            .withIcon(PlatformIcons.PACKAGE_ICON)
            .add()
    }

    private fun addPackageOrClassAccess(qualifierName: String) {
        if (qualifierName.last() != '.') {
            throw IllegalArgumentException("qualifier name must ends with dot!")
        }
        val qualifierNameLen = qualifierName.length
        if (qualifierName.startsWith("scripts")) {
            val packageNames = mutableSetOf<String>()
            val packageName = qualifierName.substring(0, qualifierName.length - 1)
            ZenScriptScriptFileIndex.processAllKeys(project) {
                if (it == packageName) {
                    val classes = findScriptFile(project, it)?.scriptBody?.classes
                        ?: return@processAllKeys true
                    for (zenClazz in classes) {
                        if (!zenClazz.isValid || zenClazz.name == null) {
                            return@processAllKeys true
                        }
                        LookupElementBuilder
                            .create(zenClazz, zenClazz.name!!)
                            .withIcon(zenClazz.getIcon(0))
                            .appendTailText(" (${(zenClazz.containingFile as ZenScriptFile).name})", true)
                            .add()
                    }

                    return@processAllKeys false
                }
                if (!it.startsWith(qualifierName)) {
                    return@processAllKeys true
                }
                val name = it.substring(qualifierNameLen).substringBefore('.')
                packageNames.add(name)

                true
            }
            for (name in packageNames) {
                addPackageCandidate(name, qualifierName + name)
            }

        } else {
            val packageNames = mutableSetOf<String>()
            ZenScriptClassNameIndex.processAllKeys(project) {
                if (!it.startsWith(qualifierName)) {
                    return@processAllKeys true
                }
                val last = it.substring(qualifierNameLen)
                val dotIndex = last.indexOf('.')
                if (dotIndex > 0) {
                    val name = last.substring(0, dotIndex)
                    if (name !in packageNames) {
                        packageNames.add(name)
                        addPackageCandidate(name, qualifierName + name)
                    }

                } else {
                    addJavaClassCandidate(last, it)
                }
                true
            }
        }

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

                    findStaticMembers(project, qualifierTarget[0].element) {
                        it.add()
                    }
                    return
                }
                qualifierType = getTargetType(qualifierTarget, false)
            }
        }

        if (qualifierType == null) {
            qualifierType = getType(qualifierExpr)
        }


        if (qualifierType is ZenScriptPackageType) {
            // addPackages
            val packageName = "${qualifierType.packageName}."
            addPackageOrClassAccess(packageName)
        }

        findMembers(project, qualifierType) {
            it.add()
        }

    }

    private fun allRootPackages(): Set<String> {
        val result = mutableSetOf<String>()
        ZenScriptClassNameIndex.processAllKeys(project) {
            val rootPackageName = it.substringBefore('.')
            result.add(rootPackageName)
            true
        }
        return result
    }

    private fun existsClass(classFQN: String): Boolean {
        var found = false
        ZenScriptClassNameIndex.processAllKeys(project) {
            if (it == classFQN) {
                found = true
                false
            } else {
                true
            }
        }
        return found
    }

    private fun addLocalClasses(start: PsiElement, classOnly: Boolean = true) {
        PsiTreeUtil.treeWalkUp(ZenScriptScopeProcessor { element, parent, _ ->
            if (element is ZenScriptClassDeclaration && element.name != null) {
                LookupElementBuilder
                    .create(element, element.name!!)
                    .withIcon(element.getIcon(0))
                    .appendTailText(" (${(element.containingFile as ZenScriptFile).name})", true)
                    .add()
            } else if (element is ZenScriptImportDeclaration && element.name != null) {
                val found = element.importReference?.resolve()
                val qualifierName = element.importReference?.qualifiedName?.qualifier
                val name = element.name!!
                var builder = if (found != null) {
                    LookupElementBuilder
                        .create(found, name)
                        .withIcon(found.getIcon(0))
                } else {
                    LookupElementBuilder
                        .create(element, name)
                        .withIcon(PlatformIcons.CLASS_ICON)
                }
                if (qualifierName != null) {
                    builder = builder.appendTailText(" ($qualifierName)", true)
                }
                builder.add()
            } else if (!classOnly && element is ZenScriptNamedElement && element.name != null) {
                when (element) {
                    is ZenScriptVariableDeclaration -> {
                        element.createLookupElement().add()
                    }

                    is ZenScriptFunctionDeclaration -> {
                        element.createLookupElement().add()
                    }

                    is ZenScriptForeachVariableDeclaration -> {

                        LookupElementBuilder
                            .createWithIcon(element)
                            .withTypeText(element.type.displayName, true)
                            .add()
                    }

                    is ZenScriptParameter -> {

                        LookupElementBuilder
                            .createWithIcon(element)
                            .withTypeText(element.type.displayName, true)
                            .add()
                    }

                    else -> {
                        throw IllegalStateException("unknown:$element")
                    }
                }

            }
            true
        }, start, null, ResolveState.initial())

    }

    private fun completeTypeRef(element: ZenScriptClassTypeRef) {
        val qualifiedName = element.qualifiedName!!
        val qualifier = qualifiedName.qualifier
        if (qualifier == null) {
            addRootPackages()
            addLocalClasses(element)
            return
        } else {
            addPackageOrClassAccess(qualifier.text)
        }
    }

    private fun completeImportRef(element: ZenScriptImportReference) {
        val qualifiedName = element.qualifiedName!!
        if (qualifiedName.qualifier == null) {
            addRootPackages()
            return
        }
        val qualifier = qualifiedName.qualifier!!.text
        if (existsClass(qualifier)) {
            if (qualifier.startsWith("scripts")) {
                findZenClass(project, qualifier)
            } else {
                findJavaClass(project, qualifier)
            }?.let {
                findStaticMembers(project, element) {
                    it.add()
                }
            }
            return
        } else {
            addPackageOrClassAccess(qualifier)
        }

    }

    private fun addRootPackages() {
        allRootPackages().forEach {
            LookupElementBuilder.create(it)
                .withIcon(PlatformIcons.PACKAGE_ICON)
                .add()
        }


        LookupElementBuilder.create("scripts")
            .withIcon(PlatformIcons.PACKAGE_ICON)
            .add()

    }

    private fun addGlobals(file: VirtualFile) {
        val index = FileBasedIndex.getInstance()
        val names = index.getAllKeys(ZenScriptGlobalVariableIndex.NAME, project)
        index.getFilesWithKey(ZenScriptGlobalVariableIndex.NAME, names.toSet(), {
            if (it == file) {
                return@getFilesWithKey true
            }
            PsiManager.getInstance(project).findFile(it)?.let { psiFile ->
                (psiFile as? ZenScriptFile)?.scriptBody?.statements
                    ?.asSequence()
                    ?.filterIsInstance<ZenScriptVariableDeclaration>()
                    ?.filter { decl -> decl.hasGlobalModifier }
                    ?.forEach { variable ->
                        variable.createLookupElement().add()
                    }
            }
            true
        }, GlobalSearchScope.projectScope(project))

        ZenScriptGlobalData.getInstance(project).getGlobalFunctions().forEach { (name, funcs) ->
            for (func in funcs) {
                if (func is PsiMethod) {
                    func.createLookupElement(name).add()
                }
            }
        }

        ZenScriptGlobalData.getInstance(project).getGlobalFields().forEach { (name, fields) ->
            for (field in fields) {
                if (field is PsiField) {
                    field.createLookupElement(name).add()
                } else if (field is PsiMethod) {
                    field.createGetterFieldLookupElement(name).add()
                }
            }
        }

    }

    private fun completeLocalAccessExpr(element: ZenScriptLocalAccessExpression) {
        addRootPackages()
        addLocalClasses(element, false)
        addGlobals(element.containingFile.originalFile.virtualFile)
    }

}