package com.warmthdawn.zenscript.type

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.elementType
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.index.ZenScriptClassNameIndex
import com.warmthdawn.zenscript.psi.ZenScriptArrayTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptClassTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptFunctionTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptListTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptMapTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptPrimitiveTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptTypes

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
    val qualifiedName = zenScriptClassType.text
    if(resolved != null) {
        return ZenScriptClassType(qualifiedName)
    }
    return ZenUnknownType(qualifiedName)
//    val project = zenScriptClassType.project
//    var found = false
//    FileBasedIndex.getInstance().getFilesWithKey(ZenScriptClassNameIndex.NAME, setOf(qualifiedName), {
//        found = true
//        false
//    }, GlobalSearchScope.projectScope(project))
//    if (!found) {
//        return ZenUnknownType(qualifiedName)
//    }
//    return ZenScriptClassType(qualifiedName)
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