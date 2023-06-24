package com.warmthdawn.zenscript.type

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.PlatformIcons
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.completion.ClassTest
import com.warmthdawn.zenscript.index.*
import com.warmthdawn.zenscript.psi.ZenScriptClassDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptFile
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.reference.ZenResolveResultType
import com.warmthdawn.zenscript.reference.ZenScriptElementResolveResult
import com.warmthdawn.zenscript.util.hasStaticModifier
import com.warmthdawn.zenscript.util.returnType
import com.warmthdawn.zenscript.util.type
import org.jetbrains.kotlin.idea.util.findModule


private fun ZenScriptPropertyMember.createLookupElement(name: String): LookupElementBuilder {
    val prop = this
    var builder = LookupElementBuilder
        .create(prop.getter ?: prop.field ?: prop.setter!!, name)
        .withIcon(PlatformIcons.PROPERTY_ICON)
    val javaType = prop.javaType

    if (javaType is PsiClassType) {
        if (isFunctionalInterface(javaType.resolve())) {
            // TODO
        }
    }

    if (prop.getter != null || prop.setter != null) {
        val from = buildString {
            append(" (From ")
            var first = true
            if (prop.getter != null) {
                append(prop.getter.name)
                append("()")
                first = false
            }
            if (prop.setter != null) {
                if (!first) {
                    append("/")
                }
                append(prop.setter.name)
                append("()")
            }
            if (prop.field != null) {
                append("/")
                append(prop.field.name)
            }
            append(")")
        }

        builder = builder.appendTailText(from, true)
    }

    builder = builder.withTypeText(ZenType.fromJavaType(javaType).displayName, true)
    return builder
}

private fun PsiMethod.createLookupElement(name: String): LookupElementBuilder {
    val method = this
    var builder = LookupElementBuilder
        .create(method, name)
        .withIcon(method.getIcon(0))


    val params = method.parameterList.parameters.joinToString(", ", "(", ")") {
        it.name + " as " + ZenType.fromJavaType(it.type).displayName
    }

    builder = builder.withTailText(params, true)
    builder = builder.withTypeText(ZenType.fromJavaType(method.returnType).displayName)
    return builder
}

private fun createNativeLookupElement(element: PsiElement, name: String, preferField: Boolean): LookupElementBuilder {

    var builder = LookupElementBuilder
        .create(element, name)
        .withIcon(element.getIcon(0))

    if (element is PsiMethod && !preferField) {
        val params = element.parameterList.parameters.joinToString(", ", "(", ")") {
            it.name + " as " + ZenType.fromJavaType(it.type).displayName
        }

        builder = builder.withTailText(params, true)
    }

    if (element is PsiMethod) {
        builder = builder.withTypeText(ZenType.fromJavaType(element.returnType).displayName)
    } else if (element is PsiField) {
        builder = builder.withTypeText(ZenType.fromJavaType(element.type).displayName)
    }

    return builder
}

fun findStaticMembers(
    project: Project,
    element: PsiElement,
    elementCollector: (name: LookupElementBuilder) -> Unit
) {
    when (element) {
        is ZenScriptClassDeclaration -> {
            element.variables.asSequence()
                .filter { it.isValid && it.hasStaticModifier }
                .forEach {
                    elementCollector(
                        LookupElementBuilder
                            .createWithIcon(it)
                            .withTypeText(ZenType.fromTypeRef(it.typeRef).displayName, true)
                    )
                }
        }

        is PsiClass -> {
            val members = ZenScriptMemberCache.getInstance(project).getMembers(element)
            members.staticProperties.forEach { (name, prop) ->
                elementCollector(prop.createLookupElement(name))
            }

            members.staticMethods.forEach { (name, methods) ->
                for (method in methods.methods) {
                    if (!method.isValid) {
                        continue
                    }

                    elementCollector(method.createLookupElement(name))
                }
            }
        }
    }

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

private fun List<PsiElement>.createResult(
    type: ZenResolveResultType,
    isValid: Boolean = true
): List<ZenScriptElementResolveResult> {
    return this.map { ZenScriptElementResolveResult(it, type, isValid) }
}


fun findMembers(project: Project, type: ZenType, elementCollector: (name: LookupElementBuilder) -> Unit) {
//    val result = mutableListOf<Pair<String, PsiElement>>()
    val memberCache = ZenScriptMemberCache.getInstance(project)
    when (type) {
        is ZenScriptPackageType -> {
            // processing outside
        }

        is ZenScriptArrayType -> {
            elementCollector(
                LookupElementBuilder.create("length")
                    .withTypeText("int")
                    .withIcon(PlatformIcons.FIELD_ICON)
            )
        }

        is ZenScriptClassType -> {
            if (type.isLibrary) {
                findJavaClass(project, type.qualifiedName)?.let {
                    memberCache.getMembers(it)
                }?.let {
                    it.properties.forEach { (name, prop) ->
                        elementCollector(prop.createLookupElement(name))
                    }
                    it.methods.forEach { (name, methods) ->
                        for (method in methods.methods) {
                            if (!method.isValid) {
                                continue
                            }
                            elementCollector(method.createLookupElement(name))
                        }
                    }
                }
            } else {
                findZenClass(project, type.qualifiedName)?.let { zenClazz ->

                    for (variable in zenClazz.variables) {
                        if (!variable.isValid) {
                            continue
                        }
                        elementCollector(
                            LookupElementBuilder
                                .createWithIcon(variable)
                                .withTypeText(variable.type.displayName, true)
                        )
                    }


                    for (function in zenClazz.functions) {
                        if (!function.isValid) {
                            continue
                        }
                        var builder = LookupElementBuilder
                            .createWithIcon(function)

                        (function.parameters?.parameterList ?: emptyList()).joinToString(", ", "(", ")") {
                            it.name + " as " + it.type.displayName
                        }.let {
                            builder = builder.withTailText(it)
                        }
                        builder = builder.withTypeText(function.returnType.displayName, true)
                        elementCollector(builder)
                    }
                }


            }

        }


        is ZenScriptListType -> {
            memberCache.getNativeMember(CraftTweakerNativeMember.LIST_SIZE).forEach {
                elementCollector(createNativeLookupElement(it, "length", true).withTypeText("int"))
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.LIST_REMOVE).forEach {
                elementCollector(createNativeLookupElement(it, "remove", false).withTypeText("void"))
            }


        }

        is ZenScriptMapType -> {

            memberCache.getNativeMember(CraftTweakerNativeMember.MAP_KEYSET).forEach {
                val keyType = ZenScriptArrayType(type.keyType).displayName
                elementCollector(createNativeLookupElement(it, "keys", false).withTypeText(keyType))
                elementCollector(createNativeLookupElement(it, "keySet", false).withTypeText(keyType))
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.MAP_VALUES).forEach {
                val valueType = ZenScriptArrayType(type.valueType).displayName
                elementCollector(createNativeLookupElement(it, "values", false).withTypeText(valueType))
                elementCollector(createNativeLookupElement(it, "valueSet", false).withTypeText(valueType))
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.MAP_ENTRYSET).forEach {
                val entryType = ZenScriptMapEntryType(type.keyType, type.valueType).displayName
                elementCollector(createNativeLookupElement(it, "entrySet", false).withTypeText(entryType))
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.MAP_SIZE).forEach {
                elementCollector(createNativeLookupElement(it, "length", true).withTypeText("int"))
            }
        }

        is ZenScriptIntRangeType -> {

            memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_FROM).forEach {
                elementCollector(createNativeLookupElement(it, "from", true).withTypeText("int"))
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_TO).forEach {
                elementCollector(createNativeLookupElement(it, "to", true).withTypeText("int"))
            }
        }

        is ZenPrimitiveType -> {
            if (type == ZenPrimitiveType.STRING) {
                memberCache.getStringNativeMethods().forEach {
                    if (!it.isValid || it.isConstructor) {
                        return@forEach
                    }
                    elementCollector(createNativeLookupElement(it, it.name, false))
                }
            }
        }

        is ZenScriptMapEntryType -> {
            memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_KEY).forEach {
                elementCollector(createNativeLookupElement(it, "key", true))
            }
            memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_VALUE).forEach {
                elementCollector(createNativeLookupElement(it, "value", true))
            }
        }

    }

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
                            .map {
                                ZenScriptElementResolveResult(
                                    it,
                                    ZenResolveResultType.ZEN_VARIABLE,
                                    !it.hasStaticModifier
                                )
                            },
                        zenClazz.functions.asSequence()
                            .filter { it.name == name }
                            .map { ZenScriptElementResolveResult(it, ZenResolveResultType.ZEN_METHOD, false) },
                    ).flatten().toList()
                }


            }

        }

        is ZenScriptArrayType -> null
        is ZenScriptListType -> when (name) {
            "length" -> memberCache.getNativeMember(CraftTweakerNativeMember.LIST_SIZE)
                .createResult(ZenResolveResultType.JAVA_PROPERTY)

            "remove" -> memberCache.getNativeMember(CraftTweakerNativeMember.LIST_REMOVE)
                .createResult(ZenResolveResultType.JAVA_METHODS, false)

            else -> null
        }

        is ZenScriptMapType -> when (name) {
            "keys", "keySet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_KEYSET)
                .createResult(ZenResolveResultType.JAVA_METHODS, false)

            "values", "valueSet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_VALUES)
                .createResult(ZenResolveResultType.JAVA_METHODS, false)

            "entrySet" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_ENTRYSET)
                .createResult(ZenResolveResultType.JAVA_METHODS, false)

            "length" -> memberCache.getNativeMember(CraftTweakerNativeMember.MAP_SIZE)
                .createResult(ZenResolveResultType.JAVA_PROPERTY)

            else -> null
        }

        is ZenScriptIntRangeType -> when (name) {
            "from" -> memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_FROM)
                .createResult(ZenResolveResultType.JAVA_PROPERTY)

            "to" -> memberCache.getNativeMember(CraftTweakerNativeMember.RANGE_TO)
                .createResult(ZenResolveResultType.JAVA_PROPERTY)

            else -> null
        }

        is ZenPrimitiveType -> when (type) {
            ZenPrimitiveType.STRING -> memberCache.getStringNativeMethods(name)
                .createResult(ZenResolveResultType.JAVA_METHODS, false)

            else -> null
        }

        is ZenScriptMapEntryType -> when (name) {
            "key" -> memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_KEY)
                .createResult(ZenResolveResultType.JAVA_PROPERTY)

            "value" -> memberCache.getNativeMember(CraftTweakerNativeMember.ENTRY_VALUE)
                .createResult(ZenResolveResultType.JAVA_PROPERTY)

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
    val javaPsiFacade = JavaPsiFacade.getInstance(project)
    var result: PsiClass? = null
    var javaName: String? = null
    var file: VirtualFile? = null

    FileBasedIndex.getInstance().processValues(ZenScriptClassNameIndex.NAME, qualifiedName, null, { f, n ->
        javaName = n
        file = f
        false
    }, GlobalSearchScope.allScope(project))
    if (file == null || javaName == null) {
        return null
    }
    val isInner = ClassFileViewProvider.isInnerClass(file!!)
    result = if (isInner) {
        javaPsiFacade.findClass(javaName!!, GlobalSearchScope.allScope(project))
    } else {
        javaPsiFacade.findClass(javaName!!, GlobalSearchScope.fileScope(project, file))
    }
    return result
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
    }, GlobalSearchScope.projectScope(project))

    return result
}