package com.warmthdawn.zenscript.type

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.warmthdawn.zenscript.index.ZenScriptMemberCache
import com.warmthdawn.zenscript.psi.ZenScriptFunctionDeclaration


class ZenScriptTypeService(val project: Project) {


    companion object {
        fun getInstance(project: Project): ZenScriptTypeService {
            return project.getService(ZenScriptTypeService::class.java)
        }
    }

    fun selectMethod(arguments: List<ZenType>, candidateMethods: List<PsiElement>, forCompletion: Boolean = false): Int {
        if (candidateMethods.isEmpty())
            return -1


        var bestPriority = ZenCallPriority.INVALID
        var bestFunctionIndex = -1
        var isValid = false

        for (i in candidateMethods.indices) {
            val priority = when (val method = candidateMethods[i]) {
                is PsiMethod -> getCallPriority(arguments, method, forCompletion)
                is ZenScriptFunctionDeclaration -> getCallPriority(arguments, method, forCompletion)
                else -> ZenCallPriority.INVALID
            }

            if (priority == bestPriority) {
                isValid = false
            } else if (priority.priority > bestPriority.priority) {
                isValid = true
                bestFunctionIndex = i
                bestPriority = priority
            }
        }

        if (isValid) {
            return bestFunctionIndex
        }

        return if (bestPriority != ZenCallPriority.INVALID) {
            // multi method available
            -bestFunctionIndex - 2
        } else -1

    }


    fun getCallPriority(arguments: List<ZenType>, javaFunc: PsiMethod, forCompletion: Boolean = false): ZenCallPriority {
        val parameters = javaFunc.parameterList.parameters

        var firstOptionalIndex = -1
        val isVarargs = javaFunc.isVarArgs
        val parameterTypes = ArrayList<ZenType>(parameters.size)
        for (i in parameters.indices) {
            val paramType = ZenType.fromJavaType(parameters[i].type)
            parameterTypes.add(paramType)
            if (parameters[i].hasAnnotation("stanhebben.zenscript.annotations.Optional")) {
                firstOptionalIndex = i
            }
        }

        return getCallPriority(arguments, parameterTypes, firstOptionalIndex, isVarargs, forCompletion)

    }

    fun getCallPriority(arguments: List<ZenType>, zsFunc: ZenScriptFunctionDeclaration, forCompletion: Boolean = false): ZenCallPriority {
        var firstOptionalIndex = -1
        val parameters = zsFunc.parameters!!.parameterList
        val parameterTypes = ArrayList<ZenType>(parameters.size)
        for (i in parameters.indices) {
            val paramType = ZenType.fromTypeRef(parameters[i].typeRef)
            parameterTypes.add(paramType)
            if (parameters[i].initializer != null) {
                firstOptionalIndex = i
            }
        }

        return getCallPriority(arguments, parameterTypes, firstOptionalIndex, false, forCompletion)
    }

    fun getCallPriority(arguments: List<ZenType>, funcType: ZenScriptFunctionType, forCompletion: Boolean = false): ZenCallPriority {
        return getCallPriority(arguments, funcType.parameterTypes, -1, false, forCompletion)
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
            val sourceJavaType = findJavaClass(project, sourceType.qualifiedName) ?: return false
            val targetJavaType = findJavaClass(project, targetType.qualifiedName) ?: return false

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
            val sourceElementType = if (sourceType is ZenScriptArrayType) sourceType.elementType else (sourceType as ZenScriptListType).elementType
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
            ZenPrimitiveType.INT to setOf(ZenPrimitiveType.BYTE, ZenPrimitiveType.SHORT, ZenPrimitiveType.LONG, ZenPrimitiveType.FLOAT, ZenPrimitiveType.DOUBLE, ZenPrimitiveType.STRING),
            ZenPrimitiveType.BYTE to setOf(ZenPrimitiveType.SHORT, ZenPrimitiveType.INT, ZenPrimitiveType.LONG, ZenPrimitiveType.FLOAT, ZenPrimitiveType.DOUBLE, ZenPrimitiveType.STRING),
            ZenPrimitiveType.SHORT to setOf(ZenPrimitiveType.BYTE, ZenPrimitiveType.INT, ZenPrimitiveType.LONG, ZenPrimitiveType.FLOAT, ZenPrimitiveType.DOUBLE, ZenPrimitiveType.STRING),
            ZenPrimitiveType.LONG to setOf(ZenPrimitiveType.BYTE, ZenPrimitiveType.SHORT, ZenPrimitiveType.INT, ZenPrimitiveType.FLOAT, ZenPrimitiveType.DOUBLE, ZenPrimitiveType.STRING),
            ZenPrimitiveType.FLOAT to setOf(ZenPrimitiveType.BYTE, ZenPrimitiveType.SHORT, ZenPrimitiveType.INT, ZenPrimitiveType.LONG, ZenPrimitiveType.DOUBLE, ZenPrimitiveType.STRING),
            ZenPrimitiveType.DOUBLE to setOf(ZenPrimitiveType.BYTE, ZenPrimitiveType.SHORT, ZenPrimitiveType.INT, ZenPrimitiveType.LONG, ZenPrimitiveType.FLOAT, ZenPrimitiveType.STRING),
            ZenPrimitiveType.STRING to setOf(ZenPrimitiveType.BOOL, ZenPrimitiveType.BYTE, ZenPrimitiveType.SHORT, ZenPrimitiveType.INT, ZenPrimitiveType.LONG, ZenPrimitiveType.FLOAT),
            ZenPrimitiveType.BOOL to setOf(ZenPrimitiveType.STRING),
    )


    private fun hasInternalCaster(targetType: ZenType, sourceType: ZenType): Boolean {
        return targetType in (internalCasters[sourceType] ?: return false)
    }

    private fun getCallPriority(arguments: List<ZenType>, parameterTypes: List<ZenType>, firstOptionalIndex: Int, isVarargs: Boolean, isForCompletion: Boolean): ZenCallPriority {
        var result: ZenCallPriority = ZenCallPriority.HIGH
        if (arguments.size > parameterTypes.size) {
            if (isVarargs) {
                val lastType: ZenType = parameterTypes[parameterTypes.size - 1]
                val varargsElementType: ZenType = (lastType as ZenScriptArrayType).elementType
                for (i in parameterTypes.size - 1 until arguments.size) {
                    val argType: ZenType = arguments[i]
                    if (argType == varargsElementType) {
                        // OK
                    } else if (canCast(argType, varargsElementType)) {
                        result = ZenCallPriority.min(result, ZenCallPriority.LOW)
                    } else if (isForCompletion && argType is ZenUnknownType) {
                        result = ZenCallPriority.min(result, ZenCallPriority.LOW)
                    } else {
                        return ZenCallPriority.INVALID
                    }
                }
            } else {
                return ZenCallPriority.INVALID
            }
        } else if (arguments.size < parameterTypes.size) {
            result = ZenCallPriority.MEDIUM
            if (firstOptionalIndex >= 0 && arguments.size < firstOptionalIndex && !isForCompletion) {
                return ZenCallPriority.INVALID
            }
        }
        var checkUntil = arguments.size
        if (isVarargs) checkUntil = Math.min(checkUntil, parameterTypes.size - 1)
        if (arguments.size == parameterTypes.size && isVarargs) {
            val lastType: ZenType = parameterTypes[parameterTypes.size - 1]
            val varargsElementType: ZenType = (lastType as ZenScriptArrayType).elementType
            val argType: ZenType = arguments[arguments.size - 1]
            if (argType == lastType || argType == varargsElementType) {
                // OK
            } else if (canCast(argType, lastType) || canCast(argType, varargsElementType)) {
                result = ZenCallPriority.min(result, ZenCallPriority.LOW)
            } else if (isForCompletion && argType is ZenUnknownType) {
                result = ZenCallPriority.min(result, ZenCallPriority.LOW)
            } else {
                return ZenCallPriority.INVALID
            }
            checkUntil = arguments.size - 1
        }
        for (i in 0 until checkUntil) {
            val argType: ZenType = arguments[i]
            val paramType: ZenType = parameterTypes[i]
            if (argType != paramType) {
                result = if (canCast(argType, paramType)) {
                    ZenCallPriority.min(result, ZenCallPriority.LOW)
                } else if (isForCompletion && argType is ZenUnknownType) {
                    ZenCallPriority.min(result, ZenCallPriority.LOW)
                } else {
                    return ZenCallPriority.INVALID
                }
            }
        }
        return result
    }
}

enum class ZenCallPriority(val priority: Int) {

    INVALID(-1),
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