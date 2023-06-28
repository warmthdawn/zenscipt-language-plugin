package com.warmthdawn.zenscript.type

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.psi.*
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.warmthdawn.zenscript.index.ZenScriptMemberCache
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.reference.ZenResolveResultType
import org.jetbrains.kotlin.caches.project.CachedValue

interface ZenType {
    val simpleName: String
    val displayName: String

    companion object {
        fun fromJavaType(type: PsiType?): ZenType {
            return when (type) {
                null -> ZenUnknownType("<unknown>")
                is PsiPrimitiveType -> ZenPrimitiveType.fromJavaPrimitive(type)
                is PsiArrayType -> ZenScriptArrayType(fromJavaType(type.componentType))
                is PsiClassType -> {
                    getClassType(type)
                }

                else -> null
            } ?: ZenUnknownType(type!!.canonicalText)

        }

        fun fromTypeRef(typeRef: ZenScriptTypeRef?): ZenType {
            if (typeRef == null) {
                return ZenUnknownType("<unknown>")
            }
            return CachedValuesManager.getProjectPsiDependentCache(typeRef) {
                when (typeRef) {
                    is ZenScriptClassTypeRef -> getClassType(typeRef)
                    is ZenScriptArrayTypeRef -> ZenScriptArrayType(fromTypeRef(typeRef.typeRef))
                    is ZenScriptListTypeRef -> ZenScriptListType(fromTypeRef(typeRef.typeRef))
                    is ZenScriptMapTypeRef -> ZenScriptMapType(
                        fromTypeRef(typeRef.keyType),
                        fromTypeRef(typeRef.valueType)
                    )

                    is ZenScriptPrimitiveTypeRef -> ZenPrimitiveType.fromTypeRef(typeRef)
                        ?: throw IllegalArgumentException("Unknown type ref kind: $typeRef")

                    is ZenScriptFunctionTypeRef -> ZenPrimitiveType.ANY // TODO: function
                    else -> throw IllegalArgumentException("Unknown type ref kind: $typeRef")
                }
            }
        }
    }
}

private fun getClassType(zenScriptClassType: ZenScriptClassTypeRef): ZenType {
    val resolved = zenScriptClassType.advancedResolve()
    val nameOrQualifiedName = zenScriptClassType.text
    if (resolved.size == 1) {
        val firstType = resolved[0].type
        if (firstType == ZenResolveResultType.ZEN_CLASS || firstType == ZenResolveResultType.JAVA_CLASS) {
            return getTargetType(resolved, true) ?: ZenUnknownType(nameOrQualifiedName)
        }
    }
    return ZenUnknownType(nameOrQualifiedName)
}

private fun getClassType(psiClassType: PsiClassType): ZenType? {

    val clazz = psiClassType.resolve() ?: return null

    if (clazz.qualifiedName == "java.util.List") {
        return ZenScriptListType(ZenType.fromJavaType(psiClassType.parameters[0]))
    }

    if (clazz.qualifiedName == "java.util.Map") {
        return ZenScriptMapType(
            ZenType.fromJavaType(psiClassType.parameters[0]),
            ZenType.fromJavaType(psiClassType.parameters[1])
        )
    }
    return when (clazz.qualifiedName) {
        "java.lang.Byte" -> ZenPrimitiveType.BYTE
        "java.lang.Short" -> ZenPrimitiveType.SHORT
        "java.lang.Integer" -> ZenPrimitiveType.INT
        "java.lang.Long" -> ZenPrimitiveType.LONG
        "java.lang.Float" -> ZenPrimitiveType.FLOAT
        "java.lang.Double" -> ZenPrimitiveType.DOUBLE
        "java.lang.Boolean" -> ZenPrimitiveType.BOOL
        "java.lang.String" -> ZenPrimitiveType.STRING
        "stanhebben.zenscript.value.IAny" -> ZenPrimitiveType.ANY
        "stanhebben.zenscript.value.IntRange" -> ZenScriptIntRangeType
        else -> {
            val zenClazzName = clazz.getAnnotation("stanhebben.zenscript.annotations.ZenClass")
                ?.let { AnnotationUtil.getStringAttributeValue(it, "value") }
                ?: return clazz.qualifiedName?.let { ZenScriptClassType(it, true) }
            ZenScriptClassType(zenClazzName)

        }
    }


}