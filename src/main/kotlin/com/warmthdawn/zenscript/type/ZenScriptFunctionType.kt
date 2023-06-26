package com.warmthdawn.zenscript.type

import com.intellij.psi.PsiMethod
import java.util.StringJoiner

open class ZenScriptFunctionType(val parameterTypes: List<ZenType>, val returnType: ZenType) : ZenType {

    override val simpleName: String
        get() = "function(${parameterTypes.joinToString(",") { it.simpleName }})${returnType.simpleName}"
    override val displayName: String
        get() = "function(${parameterTypes.joinToString(",") { it.displayName }})${returnType.displayName}"

    companion object {

        fun fromJavaMethod(method: PsiMethod): ZenScriptFunctionTypeNamedParameters {
            val params = method.parameterList.parameters

            val parameterTypes = ArrayList<ZenType>(params.size)
            val parameterNames = ArrayList<String>(params.size)

            var firstOptionalIndex = -1
            for ((i, psiParameter) in params.withIndex()) {
                if (firstOptionalIndex < 0 && psiParameter.hasAnnotation("stanhebben.zenscript.annotations.Optional")) {
                    firstOptionalIndex = i
                }
                parameterNames.add(psiParameter.name)
                parameterTypes.add(ZenType.fromJavaType(psiParameter.type))
            }
            val returnType = ZenType.fromJavaType(method.returnType)
            return ZenScriptFunctionTypeNamedParameters(
                parameterTypes,
                returnType,
                parameterNames,
                method.isVarArgs,

                )

        }
    }
}

class ZenScriptFunctionTypeNamedParameters(
    parameterTypes: List<ZenType>,
    returnType: ZenType,
    val parameterNames: List<String>,
    val isVarargs: Boolean = false,
    val firstOptionalIndex: Int = -1
) : ZenScriptFunctionType(parameterTypes, returnType) {

    override val displayName: String
        get() {
            val joiner = StringJoiner(", ")
            val nameLen = parameterNames.size
            for ((index, zenType) in parameterTypes.withIndex()) {
                if (index < nameLen) {
                    joiner.add("${parameterNames[index]} as ${zenType.displayName}")
                } else {
                    joiner.add(zenType.displayName)
                }
            }

            return "function($joiner)${returnType.displayName}"
        }
}