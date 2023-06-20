package com.warmthdawn.zenscript.type

import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.util.elementType
import com.warmthdawn.zenscript.psi.ZenScriptPrimitiveTypeRef
import com.warmthdawn.zenscript.psi.ZenScriptTypes

enum class ZenPrimitiveType(override val simpleName: String, override val displayName: String) : ZenType {
    VOID("void"),
    BYTE("byte"),
    SHORT("short"),
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    BOOL("bool"),
    ANY("any"),
    STRING("string"),
    NULL("null"),
    ;

    constructor(name: String) : this(name, name)

    companion object {
        fun fromTypeRef(typeRef: ZenScriptPrimitiveTypeRef): ZenPrimitiveType? {
            return when(typeRef.firstChild.elementType) {
                ZenScriptTypes.K_ANY -> ANY
                ZenScriptTypes.K_BYTE -> BYTE
                ZenScriptTypes.K_SHORT -> SHORT
                ZenScriptTypes.K_INT -> INT
                ZenScriptTypes.K_LONG -> LONG
                ZenScriptTypes.K_FLOAT -> FLOAT
                ZenScriptTypes.K_DOUBLE -> DOUBLE
                ZenScriptTypes.K_BOOL -> BOOL
                ZenScriptTypes.K_STRING -> STRING
                ZenScriptTypes.K_VOID -> VOID
                else -> null
            }
        }

        fun fromJavaPrimitive(javaPrim: PsiPrimitiveType): ZenPrimitiveType? {
            return when(javaPrim) {
                PsiPrimitiveType.BYTE -> BYTE
                PsiPrimitiveType.SHORT -> SHORT
                PsiPrimitiveType.INT -> INT
                PsiPrimitiveType.LONG -> LONG
                PsiPrimitiveType.FLOAT -> FLOAT
                PsiPrimitiveType.DOUBLE -> DOUBLE
                PsiPrimitiveType.BOOLEAN -> BOOL
                PsiPrimitiveType.VOID -> VOID
                else -> null
            }
        }
    }
}