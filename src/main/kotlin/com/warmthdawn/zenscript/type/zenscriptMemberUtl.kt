package com.warmthdawn.zenscript.type

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.index.CraftTweakerNativeMember
import com.warmthdawn.zenscript.index.ZenScriptClassNameIndex
import com.warmthdawn.zenscript.index.ZenScriptMemberCache
import com.warmthdawn.zenscript.index.ZenScriptScriptFileIndex
import com.warmthdawn.zenscript.psi.ZenScriptClassDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptFile
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.reference.ZenResolveResultType
import com.warmthdawn.zenscript.reference.ZenScriptElementResolveResult
import com.warmthdawn.zenscript.util.hasStaticModifier


enum class ZenScriptMemberKind {
    METHOD,
    CONSTRUCTOR,
    PROPERTY,
    OPERATOR,
    LAMBDA,
}

fun findStaticMembers(project: Project, element: PsiElement, name: String): Array<ZenScriptElementResolveResult> {
    when (element) {
        is ZenScriptClassDeclaration -> {
            return element.variableDeclarationList.asSequence()
                    .filter { it.name == name }
                    .filter { it.hasStaticModifier }
                    .map { ZenScriptElementResolveResult(it, ZenResolveResultType.ZEN_VARIABLE }
                    .toList()
                    .toTypedArray()
        }

        is PsiClass -> {
            val members = ZenScriptMemberCache.getInstance(project).getMembers(element)
            val prop = members.staticProperties[name]
            if (prop != null) {
                val candidates = mutableListOf<PsiElement>()
                if (prop.field != null) {
                    candidates.add(prop.field)
                } else if (prop.getter != null) {
                    candidates.add(prop.getter)
                } else if (prop.setter != null) {
                    candidates.add(prop.setter)
                }

                return candidates.createResult(ZenResolveResultType.JAVA_PROPERTY).toTypedArray()
            }
            val methods = members.staticMethods[name]

            if (methods != null) {
                return methods.methods.createResult(ZenResolveResultType.JAVA_METHODS).toTypedArray()
            }
        }
    }
    return emptyArray()
}

private fun List<PsiElement>.createResult(type: ZenResolveResultType): List<ZenScriptElementResolveResult> {
    return this.map { ZenScriptElementResolveResult(it, type) }
}

fun findMembers(project: Project, type: ZenType, name: String): List<ZenScriptElementResolveResult> {
    val memberCache = ZenScriptMemberCache.getInstance(project)
    val javaFacade = JavaPsiFacade.getInstance(project)

    val results: List<ZenScriptElementResolveResult>? = when (type) {
        is ZenScriptPackageType -> {
            val packageOrClassName = "${type.packageName}.${name}"
            var isClass = false
            var found = false
            for (className in FileBasedIndex.getInstance().getAllKeys(ZenScriptClassNameIndex.NAME, project)) {
                if (className == packageOrClassName) {
                    found = true
                    isClass = true
                    break
                } else if (className.startsWith(packageOrClassName)) {
                    found = true
                    break
                }
            }
            if (!found) {
                null
            } else if (isClass) {
                val javaClazz = javaFacade.findClass(packageOrClassName, GlobalSearchScope.projectScope(project))
                if (javaClazz != null) listOf(
                        ZenScriptElementResolveResult(javaClazz, ZenResolveResultType.JAVA_CLASS)
                ) else null
            } else {
                // TODO package
                null
            }
        }

        is ZenScriptClassType -> {
            if (type.isLibrary) {
                findJavaClass(project, type.qualifiedName)?.let {
                    memberCache.getMembers(it)
                }?.let {
                    val prop = it.properties[name]

                    if (prop != null) {
                        val candidates = mutableListOf<PsiElement>()
                        if (prop.field != null) {
                            candidates.add(prop.field)
                        } else if (prop.getter != null) {
                            candidates.add(prop.getter)
                        } else if (prop.setter != null) {
                            candidates.add(prop.setter)
                        }
                        candidates.createResult( ZenResolveResultType.JAVA_PROPERTY)
                    } else {
                        it.methods[name]?.methods?.createResult(ZenResolveResultType.JAVA_METHODS)
                    }
                }
            } else {
                findZenClass(project, type.qualifiedName)?.let { zenClazz ->
                    sequenceOf(
                            zenClazz.variableDeclarationList.asSequence()
                                    .filter { it.name == name }
                                    .map { ZenScriptElementResolveResult(it, ZenResolveResultType.ZEN_VARIABLE, !it.hasStaticModifier) },
                            zenClazz.functionDeclarationList.asSequence()
                                    .filter { it.name == name }
                                    .map { ZenScriptElementResolveResult(it, ZenResolveResultType.ZEN_METHOD) },
                    ).flatten().toList()
                }


            }

        }

        is ZenScriptArrayType -> null
        is ZenScriptListType -> when (name) {
            "length" -> memberCache.getNativeMember(CraftTweakerNativeMember.LIST_SIZE).createResult(ZenResolveResultType.JAVA_PROPERTY)
            "remove" -> memberCache.getNativeMember(CraftTweakerNativeMember.LIST_REMOVE).createResult(ZenResolveResultType.ZEN_METHOD)
            else -> null
        }

        is ZenScriptMapType -> when (name) {
            "keys", "keySet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_KEYSET).createResult(ZenResolveResultType.ZEN_METHOD)
            "values", "valueSet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_VALUES).createResult(ZenResolveResultType.ZEN_METHOD)
            "entries", "entrySet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_ENTRYSET).createResult(ZenResolveResultType.ZEN_METHOD)
            "length" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_SIZE).createResult(ZenResolveResultType.JAVA_PROPERTY)
            else -> null
        }

        is ZenScriptIntRangeType -> when (name) {
            "from" -> memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_FROM).createResult(ZenResolveResultType.JAVA_PROPERTY)
            "to" -> memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_TO).createResult(ZenResolveResultType.JAVA_PROPERTY)
            else -> null
        }

        is ZenScriptPrimitiveType -> when (type) {
            ZenScriptPrimitiveType.STRING -> memberCache.getStringNativeMethods(name).createResult(ZenResolveResultType.ZEN_METHOD)
            else -> null
        }

        is ZenScriptMapEntryType -> when (name) {
            "key" -> memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_KEY).createResult(ZenResolveResultType.JAVA_PROPERTY)
            "value" -> memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_VALUE).createResult(ZenResolveResultType.JAVA_PROPERTY)
            else -> null
        }

        is ZenUnknownType -> null
        else -> null
    }

    if (results.isNullOrEmpty()) {
        return findExpansionMember(project, type, name)
    }
    return results
}

fun findExpansionMember(project: Project, type: ZenType, name: String): List<ZenScriptElementResolveResult> {
    return emptyList()
}

fun findOverridingMethods(project: Project, type: ZenType, name: String): List<PsiElement> {
    return emptyList()
}

fun selectMethod(project: Project, methods: List<PsiMethod>) {

}

fun findJavaClass(project: Project, qualifiedName: String): PsiClass? {
    return (findFile(project, qualifiedName) as? PsiClassOwner)?.let {
        it.classes.first { clazz ->
            clazz.getAnnotation("stanhebben.zenscript.annotations.ZenClass")?.let { anno ->
                AnnotationUtil.getStringAttributeValue(anno, "value")
            } == qualifiedName
        }
    }
}


fun findZenClass(project: Project, qualifiedName: String): ZenScriptClassDeclaration? {
    val name = qualifiedName.substringAfterLast('.')
    return (findFile(project, qualifiedName) as? ZenScriptFile)?.scriptBody?.let {
        it.classes.first { clazz ->
            clazz.name == name
        }
    }
}

fun findZenFileMember(project: Project, packageName: String, memberName: String): PsiElement? {
    return (findScriptFile(project, packageName))?.scriptBody?.let {
        it.classes.firstOrNull { clazz ->
            clazz.name == memberName
        } ?: it.statements.firstOrNull { stmt ->
            stmt is ZenScriptVariableDeclaration && stmt.hasStaticModifier && stmt.name == memberName
        }
    }
}

fun findScriptFile(project: Project, packageName: String): ZenScriptFile? {
    var result: ZenScriptFile? = null
    FileBasedIndex.getInstance().getFilesWithKey(ZenScriptScriptFileIndex.NAME, setOf(packageName), {
        result = PsiManager.getInstance(project).findFile(it) as? ZenScriptFile
        false
    }, GlobalSearchScope.projectScope(project))
    return result
}

private fun findFile(project: Project, qualifiedName: String): PsiFile? {
    var result: PsiFile? = null
    FileBasedIndex.getInstance().getFilesWithKey(ZenScriptClassNameIndex.NAME, setOf(qualifiedName), {
        result = PsiManager.getInstance(project).findFile(it)
        false
    }, GlobalSearchScope.everythingScope(project))

    return result
}