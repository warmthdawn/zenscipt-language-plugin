package com.warmthdawn.zenscript.type

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.warmthdawn.zenscript.psi.ZenScriptConstructorDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptFunction
import com.warmthdawn.zenscript.psi.ZenScriptFunctionDeclaration
import com.warmthdawn.zenscript.util.extractMethodMetadata


class ZenScriptTypeService(val project: Project) {


    companion object {
        fun getInstance(project: Project): ZenScriptTypeService {
            return project.getService(ZenScriptTypeService::class.java)
        }
    }

    // TODO select expand functions
    fun selectMethod(
        arguments: List<ZenType>,
        candidateMethods: List<PsiElement>,
    ): Pair<ZenCallPriority, List<Int>> {
        if (candidateMethods.isEmpty())
            return ZenCallPriority.INVALID to emptyList()


        val bestFunctionIndexes = mutableListOf<Int>()
        var bestPriority = ZenCallPriority.INVALID

        for (i in candidateMethods.indices) {
            val priority = when (val method = candidateMethods[i]) {
                is PsiMethod -> getCallPriority(arguments, method)
                is ZenScriptFunctionDeclaration, is ZenScriptConstructorDeclaration -> getCallPriority(
                    arguments,
                    method as ZenScriptFunction
                )

                else -> ZenCallPriority.INVALID
            }

            if (priority.priority > bestPriority.priority) {
                bestFunctionIndexes.clear()
                bestFunctionIndexes.add(i)
                bestPriority = priority
            } else if (priority == bestPriority) {
                bestFunctionIndexes.add(i)
            }
        }

        return bestPriority to bestFunctionIndexes

    }


    fun getCallPriority(
        arguments: List<ZenType>,
        javaFunc: PsiMethod,
    ): ZenCallPriority {
        val (parameterTypes, _, firstOptionalIndex, isVarargs) = javaFunc.extractMethodMetadata()
        return getCallPriority(arguments, parameterTypes, firstOptionalIndex, isVarargs)

    }

    fun getCallPriority(
        arguments: List<ZenType>,
        zsFunc: ZenScriptFunction,
    ): ZenCallPriority {

        val (parameterTypes, _, firstOptionalIndex) = zsFunc.extractMethodMetadata()
        return getCallPriority(arguments, parameterTypes, firstOptionalIndex, false)
    }

    fun getCallPriority(
        arguments: List<ZenType>,
        funcType: ZenScriptFunctionType,
    ): ZenCallPriority {
        return getCallPriority(arguments, funcType.parameterTypes, -1, false)
    }

    private fun isNullable(type: ZenType): Boolean {
        if (type is ZenPrimitiveType) {
            return when (type) {
                ZenPrimitiveType.VOID,
                ZenPrimitiveType.BYTE,
                ZenPrimitiveType.SHORT,
                ZenPrimitiveType.INT,
                ZenPrimitiveType.LONG,
                ZenPrimitiveType.FLOAT,
                ZenPrimitiveType.DOUBLE,
                ZenPrimitiveType.BOOL -> false

                else -> true
            }
        }
        return true
    }


    fun canCast(targetType: ZenType, sourceType: ZenType): Boolean {
        if (targetType == sourceType) {
            return true
        }

        // do not convert void or unknown

        if (targetType == ZenPrimitiveType.VOID || sourceType == ZenPrimitiveType.VOID || targetType is ZenUnknownType || sourceType is ZenUnknownType) {
            return false
        }

        if (targetType is ZenScriptFunctionType || sourceType is ZenScriptFunctionType) {
            return false
        }

        if (targetType is ZenScriptClassType && !targetType.isLibrary) {
            return false
        }
        if (sourceType is ZenScriptClassType && !sourceType.isLibrary) {
            return false
        }

        if (sourceType == ZenPrimitiveType.ANY || targetType == ZenPrimitiveType.ANY) {
            return true
        }

        if (sourceType == ZenPrimitiveType.NULL) {
            return isNullable(targetType)
        }



        if (sourceType is ZenScriptClassType && targetType is ZenScriptClassType) {
            val sourceJavaType = findJavaClass(project, sourceType) ?: return false
            val targetJavaType = findJavaClass(project, targetType) ?: return false

            return sourceJavaType.isInheritor(targetJavaType, true)

        }


        // consider predefined casters
        if (hasInternalCaster(targetType, sourceType) || hasCaster(targetType, sourceType)) {
            return true
        }


        // consider expand casters
        if (hasExpandCaster(targetType, sourceType)) {
            return true
        }

        // special for collections
        if (sourceType is ZenScriptMapType) {
            if (targetType !is ZenScriptMapType) {
                return false
            }
            // only when target is map and source is any can cast implicitly
            return sourceType.keyType == ZenPrimitiveType.ANY && sourceType.valueType == ZenPrimitiveType.ANY
        }

        if (sourceType is ZenScriptListType || sourceType is ZenScriptArrayType) {
            // list to list is not supported
            if (targetType !is ZenScriptArrayType) {
                return false
            }
            val sourceElementType =
                if (sourceType is ZenScriptArrayType) sourceType.elementType else (sourceType as ZenScriptListType).elementType
            val targetElementType = targetType.elementType
            return canCast(targetElementType, sourceElementType)
        }

        return false
    }

    private fun hasCaster(targetType: ZenType, sourceType: ZenType): Boolean {
        return false
    }

    private fun hasExpandCaster(targetType: ZenType, sourceType: ZenType): Boolean {
        return false
    }

    private val internalCasters = mapOf(
        ZenPrimitiveType.INT to setOf(
            ZenPrimitiveType.BYTE,
            ZenPrimitiveType.SHORT,
            ZenPrimitiveType.LONG,
            ZenPrimitiveType.FLOAT,
            ZenPrimitiveType.DOUBLE,
            ZenPrimitiveType.STRING
        ),
        ZenPrimitiveType.BYTE to setOf(
            ZenPrimitiveType.SHORT,
            ZenPrimitiveType.INT,
            ZenPrimitiveType.LONG,
            ZenPrimitiveType.FLOAT,
            ZenPrimitiveType.DOUBLE,
            ZenPrimitiveType.STRING
        ),
        ZenPrimitiveType.SHORT to setOf(
            ZenPrimitiveType.BYTE,
            ZenPrimitiveType.INT,
            ZenPrimitiveType.LONG,
            ZenPrimitiveType.FLOAT,
            ZenPrimitiveType.DOUBLE,
            ZenPrimitiveType.STRING
        ),
        ZenPrimitiveType.LONG to setOf(
            ZenPrimitiveType.BYTE,
            ZenPrimitiveType.SHORT,
            ZenPrimitiveType.INT,
            ZenPrimitiveType.FLOAT,
            ZenPrimitiveType.DOUBLE,
            ZenPrimitiveType.STRING
        ),
        ZenPrimitiveType.FLOAT to setOf(
            ZenPrimitiveType.BYTE,
            ZenPrimitiveType.SHORT,
            ZenPrimitiveType.INT,
            ZenPrimitiveType.LONG,
            ZenPrimitiveType.DOUBLE,
            ZenPrimitiveType.STRING
        ),
        ZenPrimitiveType.DOUBLE to setOf(
            ZenPrimitiveType.BYTE,
            ZenPrimitiveType.SHORT,
            ZenPrimitiveType.INT,
            ZenPrimitiveType.LONG,
            ZenPrimitiveType.FLOAT,
            ZenPrimitiveType.STRING
        ),
        ZenPrimitiveType.STRING to setOf(
            ZenPrimitiveType.BOOL,
            ZenPrimitiveType.BYTE,
            ZenPrimitiveType.SHORT,
            ZenPrimitiveType.INT,
            ZenPrimitiveType.LONG,
            ZenPrimitiveType.FLOAT
        ),
        ZenPrimitiveType.BOOL to setOf(ZenPrimitiveType.STRING),
    )


    private fun hasInternalCaster(targetType: ZenType, sourceType: ZenType): Boolean {
        return targetType in (internalCasters[sourceType] ?: return false)
    }

    fun getCallPriority(
        arguments: List<ZenType>,
        parameterTypes: List<ZenType>,
        firstOptionalIndex: Int,
        isVarargs: Boolean,
    ): ZenCallPriority {
        var result: ZenCallPriority = ZenCallPriority.HIGH
        if (arguments.size > parameterTypes.size) {
            if (isVarargs) {
                val lastType: ZenType = parameterTypes[parameterTypes.size - 1]
                val varargsElementType: ZenType = (lastType as ZenScriptArrayType).elementType
                for (i in parameterTypes.size - 1 until arguments.size) {
                    val argType: ZenType = arguments[i]
                    if (argType == varargsElementType) {
                        // OK
                    } else if (canCast(varargsElementType, argType)) {
                        result = ZenCallPriority.min(result, ZenCallPriority.LOW)
                    } else if (argType is ZenUnknownType) {
                        result = ZenCallPriority.min(result, ZenCallPriority.PARTIAL)
                    } else {
                        return ZenCallPriority.INVALID
                    }
                }
            } else {
                result = ZenCallPriority.PARTIAL
            }
        } else if (arguments.size < parameterTypes.size) {
            result = ZenCallPriority.MEDIUM
            if (firstOptionalIndex >= 0 && arguments.size < firstOptionalIndex) {
                result = ZenCallPriority.PARTIAL
            }
        }
        var checkUntil = arguments.size
        if (isVarargs) checkUntil = checkUntil.coerceAtMost(parameterTypes.size - 1)
        if (arguments.size == parameterTypes.size && isVarargs) {
            val lastType: ZenType = parameterTypes[parameterTypes.size - 1]
            val varargsElementType: ZenType = (lastType as ZenScriptArrayType).elementType
            val argType: ZenType = arguments[arguments.size - 1]
            if (argType == lastType || argType == varargsElementType) {
                // OK
            } else if (canCast(lastType, argType) || canCast(varargsElementType, argType)) {
                result = ZenCallPriority.min(result, ZenCallPriority.LOW)
            } else if (argType is ZenUnknownType) {
                result = ZenCallPriority.min(result, ZenCallPriority.PARTIAL)
            } else {
                return ZenCallPriority.INVALID
            }
            checkUntil = arguments.size - 1
        }
        for (i in 0 until checkUntil) {
            val argType: ZenType = arguments[i]
            val paramType: ZenType = parameterTypes[i]
            if (argType != paramType) {
                result = if (canCast(paramType, argType)) {
                    ZenCallPriority.min(result, ZenCallPriority.LOW)
                } else if (argType is ZenUnknownType) {
                    ZenCallPriority.min(result, ZenCallPriority.PARTIAL)
                } else {
                    return ZenCallPriority.INVALID
                }
            }
        }
        return result
    }
}

enum class ZenCallPriority(val priority: Int) {

    INVALID(-2),
    PARTIAL(-1),
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    companion object {
        fun max(a: ZenCallPriority, b: ZenCallPriority): ZenCallPriority {
            return if (a.priority > b.priority) a else b
        }

        fun min(a: ZenCallPriority, b: ZenCallPriority): ZenCallPriority {
            return if (a.priority < b.priority) a else b
        }
    }

}