package com.warmthdawn.zenscript.type

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.index.CraftTweakerNativeMember
import com.warmthdawn.zenscript.index.ZenScriptClassNameIndex
import com.warmthdawn.zenscript.index.ZenScriptMemberCache
import com.warmthdawn.zenscript.psi.ZenScriptClass
import com.warmthdawn.zenscript.psi.ZenScriptClassDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptFile


enum class ZenScriptMemberKind {
    METHOD,
    CONSTRUCTOR,
    PROPERTY,
    OPERATOR,
    LAMBDA,
}


fun findMember(project: Project, type: ZenType, name: String): List<PsiElement> {
    val memberCache = ZenScriptMemberCache.getInstance(project)
    val javaFacade = JavaPsiFacade.getInstance(project)

    val found: List<PsiElement>? = when (type) {
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
                if (javaClazz != null) listOf(javaClazz) else null
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
                        candidates
                    } else {
                        it.methods[name]?.methods
                    }
                }
            } else {
                findZenClass(project, type.qualifiedName)
                        ?.members?.filter { it.name == name }
            }

        }

        is ZenScriptArrayType -> null
        is ZenScriptListType -> when (name) {
            "length" -> memberCache.getNativeMember(CraftTweakerNativeMember.LIST_SIZE)
            "remove" -> memberCache.getNativeMember(CraftTweakerNativeMember.LIST_REMOVE)
            else -> null
        }

        is ZenScriptMapType -> when (name) {
            "keys", "keySet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_KEYSET)
            "values", "valueSet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_VALUES)
            "entries", "entrySet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_ENTRYSET)
            "length" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_SIZE)
            else -> null
        }

        is ZenScriptIntRangeType -> when (name) {
            "from" -> memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_FROM)
            "to" -> memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_TO)
            else -> null
        }

        is ZenScriptPrimitiveType -> when (type) {
            ZenScriptPrimitiveType.STRING -> memberCache.getStringNativeMember(name)
            else -> null
        }

        is ZenScriptMapEntryType -> when (name) {
            "key" -> memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_KEY)
            "value" -> memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_VALUE)
            else -> null
        }

        is ZenUnknownType -> null
        else -> null
    }

    if (found.isNullOrEmpty()) {
        return findExpansionMember(project, type, name)
    }
    return found
}

fun findExpansionMember(project: Project, type: ZenType, name: String): List<PsiElement> {
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

fun findFile(project: Project, qualifiedName: String): PsiFile? {
    var result: PsiFile? = null
    FileBasedIndex.getInstance().getFilesWithKey(ZenScriptClassNameIndex.NAME, setOf(qualifiedName), {
        result = PsiManager.getInstance(project).findFile(it)
        false
    }, GlobalSearchScope.everythingScope(project))

    return result
}