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

fun findStaticMembers(project: Project, element: PsiElement): List<Pair<String, PsiElement>> {
    val result = mutableListOf<Pair<String, PsiElement>>()
    when (element) {
        is ZenScriptClassDeclaration -> {
            element.variables.asSequence()
                    .filter { it.isValid && it.hasStaticModifier }
                    .forEach {
                        val name = it.name
                        if (name != null) {
                            result.add(name to it)
                        }
                    }
        }

        is PsiClass -> {
            val members = ZenScriptMemberCache.getInstance(project).getMembers(element)
            members.staticProperties.forEach { (name, prop) ->
                if (prop.getter != null) {
                    result.add(name to prop.getter)
                } else if (prop.setter != null) {
                    result.add(name to prop.setter)
                } else if (prop.field != null) {
                    result.add(name to prop.field)
                }
            }

            members.staticMethods.forEach { (name, methods) ->
                for (method in methods.methods) {
                    if (!method.isValid) {
                        continue
                    }
                    result.add(name to method)
                }
            }
        }
    }

    return result
}

fun findStaticMembers(project: Project, element: PsiElement, name: String): Array<ZenScriptElementResolveResult> {
    when (element) {
        is ZenScriptClassDeclaration -> {
            return element.variables.asSequence()
                    .filter { it.name == name }
                    .filter { it.hasStaticModifier }
                    .map { ZenScriptElementResolveResult(it, ZenResolveResultType.ZEN_VARIABLE) }
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

private fun List<PsiElement>.createResult(type: ZenResolveResultType, isValid: Boolean = true): List<ZenScriptElementResolveResult> {
    return this.map { ZenScriptElementResolveResult(it, type, isValid) }
}


fun findMembers(project: Project, type: ZenType): List<Pair<String, PsiElement>> {
    val result = mutableListOf<Pair<String, PsiElement>>()
    val memberCache = ZenScriptMemberCache.getInstance(project)
    when (type) {
        is ZenScriptPackageType -> {
            // processing outside
        }
        is ZenScriptArrayType -> {
            // processing outside
        }

        is ZenScriptClassType -> {
            if (type.isLibrary) {
                findJavaClass(project, type.qualifiedName)?.let {
                    memberCache.getMembers(it)
                }?.let {
                    it.properties.forEach { (name, prop) ->
                        if (prop.getter != null) {
                            result.add(name to prop.getter)
                        } else if (prop.setter != null) {
                            result.add(name to prop.setter)
                        } else if (prop.field != null) {
                            result.add(name to prop.field)
                        }
                    }
                    it.methods.forEach { (name, methods) ->
                        for (method in methods.methods) {
                            if (!method.isValid) {
                                continue
                            }
                            result.add(name to method)
                        }
                    }
                }
            } else {
                findZenClass(project, type.qualifiedName)?.let { zenClazz ->
                    sequenceOf(
                            zenClazz.variables.asSequence()
                                    .filter { it.isValid },
                            zenClazz.functions.asSequence()
                                    .filter { it.isValid }
                    ).flatten().forEach {
                        val name = it.name
                        if(name != null) {
                            result.add(name to it)
                        }
                    }
                }


            }

        }

        is ZenScriptListType ->  {
            memberCache.getNativeMember(CraftTweakerNativeMember.LIST_SIZE).forEach {
                result.add("length" to it)
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.LIST_REMOVE).forEach {
                result.add("remove" to it)
            }

        }
        is ZenScriptMapType -> {

            memberCache.getNativeMember(CraftTweakerNativeMember.MAP_KEYSET).forEach {
                result.add("keys" to it)
                result.add("keySet" to it)
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.MAP_VALUES).forEach {
                result.add("values" to it)
                result.add("values" to it)
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.MAP_ENTRYSET).forEach {
                result.add("entries" to it)
                result.add("entrySet" to it)
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.MAP_SIZE).forEach {
                result.add("length" to it)
            }
        }

        is ZenScriptIntRangeType -> {

            memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_FROM).forEach {
                result.add("from" to it)
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_TO).forEach {
                result.add("to" to it)
            }
        }

        is ZenPrimitiveType ->  {
            if(type == ZenPrimitiveType.STRING) {
                memberCache.getStringNativeMethods().forEach {
                    if(!it.isValid || it.isConstructor) {
                        return@forEach
                    }
                    result.add(it.name to it)
                }
            }
        }

        is ZenScriptMapEntryType -> {
            memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_KEY).forEach {
                result.add("key" to it)
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_VALUE).forEach {
                result.add("value" to it)
            }
        }

    }

    return result
}

fun findMembers(project: Project, type: ZenType, name: String): List<ZenScriptElementResolveResult> {
    val memberCache = ZenScriptMemberCache.getInstance(project)

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
                val javaClazz = findJavaClass(project, packageOrClassName)
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
                        candidates.createResult(ZenResolveResultType.JAVA_PROPERTY)
                    } else {
                        it.methods[name]?.methods?.createResult(ZenResolveResultType.JAVA_METHODS, false)
                    }
                }
            } else {
                findZenClass(project, type.qualifiedName)?.let { zenClazz ->
                    sequenceOf(
                            zenClazz.variables.asSequence()
                                    .filter { it.name == name }
                                    .map { ZenScriptElementResolveResult(it, ZenResolveResultType.ZEN_VARIABLE, !it.hasStaticModifier) },
                            zenClazz.functions.asSequence()
                                    .filter { it.name == name }
                                    .map { ZenScriptElementResolveResult(it, ZenResolveResultType.ZEN_METHOD, false) },
                    ).flatten().toList()
                }


            }

        }

        is ZenScriptArrayType -> null
        is ZenScriptListType -> when (name) {
            "length" -> memberCache.getNativeMember(CraftTweakerNativeMember.LIST_SIZE).createResult(ZenResolveResultType.JAVA_PROPERTY)
            "remove" -> memberCache.getNativeMember(CraftTweakerNativeMember.LIST_REMOVE).createResult(ZenResolveResultType.JAVA_METHODS, false)
            else -> null
        }

        is ZenScriptMapType -> when (name) {
            "keys", "keySet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_KEYSET).createResult(ZenResolveResultType.JAVA_METHODS, false)
            "values", "valueSet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_VALUES).createResult(ZenResolveResultType.JAVA_METHODS, false)
            "entries", "entrySet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_ENTRYSET).createResult(ZenResolveResultType.JAVA_METHODS, false)
            "length" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_SIZE).createResult(ZenResolveResultType.JAVA_PROPERTY)
            else -> null
        }

        is ZenScriptIntRangeType -> when (name) {
            "from" -> memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_FROM).createResult(ZenResolveResultType.JAVA_PROPERTY)
            "to" -> memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_TO).createResult(ZenResolveResultType.JAVA_PROPERTY)
            else -> null
        }

        is ZenPrimitiveType -> when (type) {
            ZenPrimitiveType.STRING -> memberCache.getStringNativeMethods(name).createResult(ZenResolveResultType.JAVA_METHODS, false)
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


fun findJavaClass(project: Project, qualifiedName: String): PsiClass? {
    return (findFile(project, qualifiedName) as? PsiClassOwner)?.let {
        it.classes.first { clazz ->
            clazz.getAnnotation("stanhebben.zenscript.annotations.ZenClass")?.let { anno ->
                AnnotationUtil.getStringAttributeValue(anno, "value")
            } == qualifiedName
        }
    }
}


fun isFunctionalInterface(javaClazz: PsiClass?): Boolean {
    javaClazz ?: return false
    return false
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