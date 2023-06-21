package com.warmthdawn.zenscript.index

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.PsiClassImplUtil
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.TypeConversionUtil
import com.warmthdawn.zenscript.type.ZenType
import java.util.*


enum class CraftTweakerNativeMember {
    ENTRY_KEY,
    ENTRY_VALUE,
    RANGE_FROM,
    RANGE_TO,
    LIST_SIZE,
    LIST_REMOVE,
    MAP_KEYSET,
    MAP_VALUES,
    MAP_ENTRYSET,
    MAP_SIZE,

}
class ZenScriptMemberCache(private val project: Project) {

    companion object {
        fun getInstance(project: Project): ZenScriptMemberCache {
            return project.getService(ZenScriptMemberCache::class.java)
        }
    }

    fun getMembers(javaClazz: PsiClass): ZenClassCacheEntry {
        return CachedValuesManager.getCachedValue(javaClazz) {
            val data = this.fetchMembers(javaClazz)
            CachedValueProvider.Result(data, javaClazz)
        }
    }

    fun getNativeMember(member: CraftTweakerNativeMember): List<PsiElement> {
        return when(member) {
            CraftTweakerNativeMember.ENTRY_KEY -> getNativeMember("java.util.Map.Entry", "getKey")
            CraftTweakerNativeMember.ENTRY_VALUE -> getNativeMember("java.util.Map.Entry", "getValue")
            CraftTweakerNativeMember.RANGE_FROM -> getNativeMember("stanhebben.zenscript.value.IntRange", "getFrom")
            CraftTweakerNativeMember.RANGE_TO -> getNativeMember("stanhebben.zenscript.value.IntRange", "getTo")
            CraftTweakerNativeMember.LIST_SIZE -> getNativeMember("java.util.List", "size")
            CraftTweakerNativeMember.LIST_REMOVE -> getNativeMember("java.util.List", "remove")
            CraftTweakerNativeMember.MAP_KEYSET -> getNativeMember("java.util.Map", "keySet")
            CraftTweakerNativeMember.MAP_VALUES -> getNativeMember("java.util.Map", "values")
            CraftTweakerNativeMember.MAP_ENTRYSET -> getNativeMember("java.util.Map", "entrySet")
            CraftTweakerNativeMember.MAP_SIZE -> getNativeMember("java.util.Map", "size")
            else -> emptyList()
        }
    }

    fun getStringNativeMethods(name: String): List<PsiElement>   {
        val javaClazz = JavaPsiFacade.getInstance(project).findClass("java.lang.String", GlobalSearchScope.allScope(project)) ?: return emptyList()
        return javaClazz.findMethodsByName(name, true).toList()
    }

    fun getStringNativeMethods(): Array<PsiMethod>   {
        val javaClazz = JavaPsiFacade.getInstance(project).findClass("java.lang.String", GlobalSearchScope.allScope(project)) ?: return emptyArray()
        return javaClazz.allMethods
    }

    private fun getNativeMember(clazz: String, name: String, isMethod: Boolean = true): List<PsiElement>   {
        val javaClazz = JavaPsiFacade.getInstance(project).findClass(clazz, GlobalSearchScope.allScope(project)) ?: return emptyList()
        return if(isMethod) {
            javaClazz.findMethodsByName(name, false).toList()
        } else {
            javaClazz.findFieldByName(name, false) ?. let { listOf(it) } ?: emptyList()
        }
    }

    private fun fetchMembers(javaClazz: PsiClass): ZenClassCacheEntry {

        val properties = mutableMapOf<String, Array<PsiMember?>>()
        val staticProperties = mutableMapOf<String, Array<PsiMember?>>()
        val methods = mutableMapOf<String, MutableList<PsiMethod>>()
        val staticMethods = mutableMapOf<String, MutableList<PsiMethod>>()

        val operators = mutableMapOf<ZenOperatorType, PsiMethod>()
        val constructors = mutableListOf<PsiMethod>()

        var iteratorKeyType = ""
        var iteratorValueType = ""
        var iteratorKind: ZenIteratorKind = ZenIteratorKind.NODE
        for (annotation in javaClazz.annotations) {
            if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.IterableSimple")) {
                iteratorKind = ZenIteratorKind.ITERABLE
                iteratorValueType = AnnotationUtil.getStringAttributeValue(annotation, "value")!!
                break
            }
            if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.IterableList")) {
                iteratorKind = ZenIteratorKind.LIST
                iteratorKeyType = "int"
                iteratorValueType = AnnotationUtil.getStringAttributeValue(annotation, "value")!!
                break
            }
            if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.IterableMap")) {
                iteratorKind = ZenIteratorKind.MAP
                iteratorKeyType = AnnotationUtil.getStringAttributeValue(annotation, "key")!!
                iteratorValueType = AnnotationUtil.getStringAttributeValue(annotation, "value")!!
                break
            }
        }



        for (method in javaClazz.allMethods) {
            val methodName = method.name
            val isStatic = method.hasModifierProperty(PsiModifier.STATIC)
            for (annotation in method.annotations) {
                if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.ZenCaster")) {
                    operators[ZenOperatorType.CASTER] = method
                } else if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.ZenMethod")) {
                    var actualName = AnnotationUtil.getStringAttributeValue(annotation, "value")
                    if (actualName.isNullOrEmpty()) {
                        actualName = methodName
                    }

                    (if (isStatic) staticMethods else methods)
                            .computeIfAbsent(actualName) { mutableListOf() }
                            .add(method)

                } else if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.ZenGetter")) {
                    if (!isValidGetter(method))
                        continue

                    var propName = AnnotationUtil.getStringAttributeValue(annotation, "value")
                    if (propName.isNullOrEmpty()) {
                        propName = methodName
                    }

                    (properties.computeIfAbsent(propName) { arrayOfNulls(3) })[1] = method

                } else if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.ZenSetter")) {
                    if (!isValidSetter(method))
                        continue

                    var propName = AnnotationUtil.getStringAttributeValue(annotation, "value")
                    if (propName.isNullOrEmpty()) {
                        propName = methodName
                    }

                    (properties.computeIfAbsent(propName) { arrayOfNulls(3) })[2] = method
                } else if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.ZenMemberGetter")) {
                    operators[ZenOperatorType.MEMBER_GETTER] = method
                } else if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.ZenMemberSetter")) {
                    operators[ZenOperatorType.MEMBER_SETTER] = method
                } else if (annotation.hasQualifiedName("stanhebben.zenscript.annotations.ZenOperator")) {
                    val op = getEnumAnnoValue(annotation, "value")
                            ?: continue
                    val opType = ZenOperatorType.fromName(op)
                            ?: continue
                    operators[opType] = method
                }
            }

        }

        for (field in javaClazz.allFields) {
            val annotation = field.getAnnotation("stanhebben.zenscript.annotations.ZenProperty")
                    ?: continue
            val isStatic = field.hasModifierProperty(PsiModifier.STATIC)
            val propsMap = (if (isStatic) staticProperties else properties)
            var propName = AnnotationUtil.getStringAttributeValue(annotation, "value")
            if (propName.isNullOrEmpty()) {
                propName = field.name
            }

            val basicMethodName = propName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


            val fieldElements = propsMap.computeIfAbsent(propName) { arrayOfNulls(3) }

            fieldElements[0] = field

            if (fieldElements[1] == null) {
                var getterName = AnnotationUtil.getStringAttributeValue(annotation, "getter")
                if (getterName.isNullOrEmpty()) {
                    getterName = if (TypeConversionUtil.isBooleanType(field.type)) "is$basicMethodName" else "get$basicMethodName"
                }

                val getterMethod = javaClazz.findMethodsByName(getterName, true)
                        .firstOrNull { it.parameterList.isEmpty }
                if (isValidGetter(getterMethod)) {
                    fieldElements[1] = getterMethod
                }
            }

            if (field.hasModifierProperty(PsiModifier.FINAL) && fieldElements[2] == null) {
                var setterName = AnnotationUtil.getStringAttributeValue(annotation, "setter")
                if (setterName.isNullOrEmpty()) {
                    setterName = "set$basicMethodName"
                }

                val setterMethod = javaClazz.findMethodsByName(setterName, true)
                        .firstOrNull {
                            val params = it.parameterList.parameters
                            params.size == 1 && params[0].type.isConvertibleFrom(field.type)
                        }
                if (isValidSetter(setterMethod)) {
                    fieldElements[2] = setterMethod
                }
            }
        }

        for (ctor in javaClazz.constructors) {
            if (ctor.hasAnnotation("stanhebben.zenscript.annotations.ZenConstructor")) {
                constructors.add(ctor)
            }
        }

        return ZenClassCacheEntry(
                properties = properties.entries.associate { it.key to createPropMember(it) },
                staticProperties = staticProperties.entries.associate { it.key to createPropMember(it) },
                methods = methods.entries.associate { it.key to createMethodMember(it) },
                staticMethods = staticMethods.entries.associate { it.key to createMethodMember(it) },
                constructors = constructors,
                operators = operators,
                iterator = Triple(iteratorKind, iteratorKeyType, iteratorValueType)
        )
    }
}

private fun createMethodMember(entry: Map.Entry<String, List<PsiMethod>>): ZenScriptMethodMember {
    return ZenScriptMethodMember(entry.key, entry.value)
}

private fun createPropMember(entry: Map.Entry<String, Array<PsiMember?>>): ZenScriptPropertyMember {
    return ZenScriptPropertyMember(entry.key, entry.value[0] as? PsiField, entry.value[1] as? PsiMethod, entry.value[2] as? PsiMethod)
}

private fun getEnumAnnoValue(annotation: PsiAnnotation, name: String): String? {
    val member = annotation.findAttributeValue(name)
    if (member !is PsiReferenceExpression) {
        return null
    }
    val resolved = member.resolve()
    if (resolved !is PsiEnumConstant) {
        return null
    }
    return resolved.name
}

private fun isValidGetter(method: PsiMethod?): Boolean {
    return true
}

private fun isValidSetter(method: PsiMethod?): Boolean {
    return true
}


private fun isValidOperator(method: PsiMethod): Boolean {
    return true
}


data class ZenClassCacheEntry(
        val properties: Map<String, ZenScriptPropertyMember>,
        val staticProperties: Map<String, ZenScriptPropertyMember>,
        val methods: Map<String, ZenScriptMethodMember>,
        val staticMethods: Map<String, ZenScriptMethodMember>,
        val iterator: Triple<ZenIteratorKind, String, String>,
        val operators: Map<ZenOperatorType, PsiMethod>,
        val constructors: List<PsiMethod>,
)

enum class ZenOperatorType(val kind: ZenOperatorKind) {
    ADD(ZenOperatorKind.BINARY),
    SUB(ZenOperatorKind.BINARY),
    MUL(ZenOperatorKind.BINARY),
    DIV(ZenOperatorKind.BINARY),
    MOD(ZenOperatorKind.BINARY),
    CAT(ZenOperatorKind.BINARY),
    OR(ZenOperatorKind.BINARY),
    AND(ZenOperatorKind.BINARY),
    XOR(ZenOperatorKind.BINARY),
    RANGE(ZenOperatorKind.BINARY),
    COMPARE(ZenOperatorKind.BINARY),
    EQUALS(ZenOperatorKind.BINARY),
    CONTAINS(ZenOperatorKind.BINARY),
    NEG(ZenOperatorKind.UNARY),
    NOT(ZenOperatorKind.UNARY),
    INDEX_SET(ZenOperatorKind.SPECIAL),
    INDEX_GET(ZenOperatorKind.SPECIAL),
    MEMBER_GETTER(ZenOperatorKind.SPECIAL),
    MEMBER_SETTER(ZenOperatorKind.SPECIAL),
    CASTER(ZenOperatorKind.SPECIAL),

    ;

    companion object {
        fun fromName(name: String): ZenOperatorType? {
            return when (name) {
                "ADD" -> ADD
                "SUB" -> SUB
                "MUL" -> MUL
                "DIV" -> DIV
                "MOD" -> MOD
                "CAT" -> CAT
                "OR" -> OR
                "AND" -> AND
                "XOR" -> XOR
                "NEG" -> NEG
                "NOT" -> NOT
                "INDEXSET" -> INDEX_SET
                "INDEXGET" -> INDEX_GET
                "RANGE" -> RANGE
                "CONTAINS" -> CONTAINS
                "COMPARE" -> COMPARE
                "EQUALS" -> EQUALS
                else -> null
            }
        }

        fun fromToken(name: String) {

        }
    }
}

enum class ZenIteratorKind {
    NODE,
    ITERABLE,
    LIST,
    MAP,
}

enum class ZenOperatorKind {
    BINARY,
    UNARY,
    SPECIAL
}

data class ZenScriptMethodMember(
        val name: String,
        val methods: List<PsiMethod>,
)


data class ZenScriptPropertyMember(
        val name: String,
        val field: PsiField?,
        val getter: PsiMethod?,
        val setter: PsiMethod?,
) {
    val javaType: PsiType
        get() = this.field?.type ?: getter?.returnType ?: setter?.parameterList?.parameters?.get(0)?.type!!

    val zsType: ZenType
        get() = ZenType.fromJavaType(this.javaType)
}

