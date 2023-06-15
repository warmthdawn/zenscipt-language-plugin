package com.warmthdawn.zenscript.type

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.psi.*
import com.warmthdawn.zenscript.psi.*

interface ZenType {
    val simpleName: String
    val displayName: String

    companion object {
        fun fromJavaType(type: PsiType?): ZenType {
            return when (type) {
                null -> ZenUnknownType("<unknown>")
                is PsiPrimitiveType -> ZenScriptPrimitiveType.fromJavaPrimitive(type)
                is PsiArrayType -> ZenScriptArrayType(fromJavaType(type.componentType))
                is PsiClassType -> {
                    getClassType(type)
                }

                else -> null
            } ?: ZenUnknownType(type!!.canonicalText)

        }

        fun fromTypeRef(typeRef: ZenScriptTypeRef?): ZenType {
            return when (typeRef) {
                is ZenScriptClassTypeRef -> getClassType(typeRef)
                is ZenScriptArrayTypeRef -> ZenScriptArrayType(fromTypeRef(typeRef.typeRef))
                is ZenScriptListTypeRef -> ZenScriptListType(fromTypeRef(typeRef.typeRef))
                is ZenScriptMapTypeRef -> ZenScriptMapType(fromTypeRef(typeRef.keyType), fromTypeRef(typeRef.valueType))
                is ZenScriptPrimitiveTypeRef -> ZenScriptPrimitiveType.fromTypeRef(typeRef)
                        ?: throw IllegalArgumentException("Unknown type ref kind: $typeRef")

                is ZenScriptFunctionTypeRef -> ZenScriptPrimitiveType.ANY // TODO: function
                null -> ZenUnknownType("<unknown>")
                else -> throw IllegalArgumentException("Unknown type ref kind: $typeRef")
            }
        }
    }
}

private fun getClassType(zenScriptClassType: ZenScriptClassTypeRef): ZenType {
    val resolved = zenScriptClassType.resolve()
    val nameOrQualifiedName = zenScriptClassType.text
    if (resolved != null) {
        if (resolved is ZenScriptClassDeclaration || resolved is PsiClass) {
            return getTargetType(resolved)
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
        "java.lang.Byte" -> ZenScriptPrimitiveType.BYTE
        "java.lang.Short" -> ZenScriptPrimitiveType.SHORT
        "java.lang.Integer" -> ZenScriptPrimitiveType.INT
        "java.lang.Long" -> ZenScriptPrimitiveType.LONG
        "java.lang.Float" -> ZenScriptPrimitiveType.FLOAT
        "java.lang.Double" -> ZenScriptPrimitiveType.DOUBLE
        "java.lang.Boolean" -> ZenScriptPrimitiveType.BOOL
        "java.lang.String" -> ZenScriptPrimitiveType.STRING
        "stanhebben.zenscript.value.IAny" -> ZenScriptPrimitiveType.ANY
        "stanhebben.zenscript.value.IntRange" -> ZenScriptIntRangeType
        else -> {
            val zenClazzName = clazz.getAnnotation("stanhebben.zenscript.annotations.ZenClass")
                    ?.let { AnnotationUtil.getStringAttributeValue(it, "value") }
                    ?: return null
            ZenScriptClassType(zenClazzName)

        }
    }


}