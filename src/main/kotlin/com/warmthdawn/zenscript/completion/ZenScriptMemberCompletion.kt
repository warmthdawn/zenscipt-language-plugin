package com.warmthdawn.zenscript.completion;

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiVariable
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PlatformIcons
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.completion.ZenScriptPatterns.AT_TOP_LEVEL
import com.warmthdawn.zenscript.completion.ZenScriptPatterns.ZEN_SCRIPT_IDENTIFIER
import com.warmthdawn.zenscript.index.ZenScriptClassNameIndex
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.reference.ZenResolveResultType
import com.warmthdawn.zenscript.reference.ZenScriptScopeProcessor
import com.warmthdawn.zenscript.type.*
import com.warmthdawn.zenscript.util.returnType
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

    private fun addClassCandidate(name: String, qualifiedName: String) {
//        name.
        if (qualifiedName.startsWith("scripts")) {
            val zenClazz = findZenClass(project, qualifiedName)!!
            LookupElementBuilder
                .create(zenClazz, name)
                .withIcon(zenClazz.getIcon(0))
                .appendTailText(" (${(zenClazz.containingFile as ZenScriptFile).name})", true)
                .add()

        } else {
            val javaClass = findJavaClass(project, qualifiedName)
                ?: throw NullPointerException("could not find $qualifiedName")
            LookupElementBuilder
                .create(javaClass, name)
                .withIcon(javaClass.getIcon(0))
                .appendTailText(" (${qualifiedName.substringBeforeLast('.')})", true)
                .add()

        }

    }

    private fun addPackageCandidate(name: String, qualifiedName: String) {

        LookupElementBuilder.create(name)
            .withTailText(" ($qualifiedName)", true)
            .withIcon(PlatformIcons.PACKAGE_ICON)
            .add()
    }

    private fun addPackageOrClassAccess(packageName: String) {
        val packageNameLen = packageName.length
        val packageNames = mutableSetOf<String>()
        for (className in FileBasedIndex.getInstance().getAllKeys(ZenScriptClassNameIndex.NAME, project)) {
            if (!className.startsWith(packageName)) {
                continue
            }
            val last = className.substring(packageNameLen)
            val dotIndex = last.indexOf('.')
            if (dotIndex > 0) {
                val name = last.substring(0, dotIndex)
                if (name !in packageNames) {
                    packageNames.add(name)
                    addPackageCandidate(name, packageName + name)
                }

            } else {
                addClassCandidate(last, className)
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

    private fun allRootPackages(): Sequence<String> {
        return FileBasedIndex.getInstance().getAllKeys(ZenScriptClassNameIndex.NAME, project)
            .asSequence()
            .map { it.substringBefore('.') }
            .distinct()
    }

    private fun existsClass(classFQN: String): Boolean {
        var found = false
        FileBasedIndex.getInstance().processAllKeys(ZenScriptClassNameIndex.NAME, {
            if (it == classFQN) {
                found = true
                false
            } else {
                true
            }
        }, project)
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
                false
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
                false
            } else if (classOnly || element !is ZenScriptNamedElement || element.name == null) {
                true
            } else {

                if (element is ZenScriptVariableDeclaration) {
                    LookupElementBuilder
                        .createWithIcon(element)
                        .withTypeText(element.type.displayName, true)
                        .add()
                } else if (element is ZenScriptFunctionDeclaration) {

                    var builder = LookupElementBuilder
                        .createWithIcon(element)

                    (element.parameters?.parameterList ?: emptyList()).joinToString(", ", "(", ")") {
                        it.name + " as " + it.type.displayName
                    }.let {
                        builder = builder.withTailText(it)
                    }
                    builder = builder.withTypeText(element.returnType.displayName, true)

                } else if (element is ZenScriptForeachVariableDeclaration) {

                    LookupElementBuilder
                        .createWithIcon(element)
                        .withTypeText(element.type.displayName, true)
                        .add()
                } else if (element is ZenScriptParameter) {

                    LookupElementBuilder
                        .createWithIcon(element)
                        .withTypeText(element.type.displayName, true)
                        .add()
                } else {
                    throw IllegalStateException("unknown:$element")
                }

                false
            }
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

    }

    private fun completeLocalAccessExpr(element: ZenScriptLocalAccessExpression) {
        addRootPackages()
        addLocalClasses(element, false)
    }

}