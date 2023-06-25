package com.warmthdawn.zenscript.completion

import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.util.MethodParenthesesHandler
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.util.PlatformIcons
import com.warmthdawn.zenscript.index.ZenScriptPropertyMember
import com.warmthdawn.zenscript.psi.ZenScriptFunctionDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.type.ZenType
import com.warmthdawn.zenscript.type.isFunctionalInterface
import com.warmthdawn.zenscript.util.returnType
import com.warmthdawn.zenscript.util.type


fun ZenScriptVariableDeclaration.createLookupElement(): LookupElementBuilder {

    return LookupElementBuilder
        .createWithIcon(this)
        .withTypeText(this.type.displayName, true)
}

fun ZenScriptFunctionDeclaration.createLookupElement(): LookupElementBuilder {

    var builder = LookupElementBuilder
        .createWithIcon(this)
        .withInsertHandler(object : ParenthesesInsertHandler<LookupElement>() {
            override fun placeCaretInsideParentheses(
                context: InsertionContext?,
                item: LookupElement?
            ): Boolean {
                return this@createLookupElement.parameters!!.parameterList.isNotEmpty()
            }
        })

    (this.parameters?.parameterList ?: emptyList()).joinToString(", ", "(", ")") {
        it.name + " as " + it.type.displayName
    }.let {
        builder = builder.withTailText(it)
    }
    builder = builder.withTypeText(this.returnType.displayName, true)

    return builder
}

fun PsiMethod.createLookupElement(name: String): LookupElementBuilder {
    val method = this
    var builder = LookupElementBuilder
        .create(method, name)
        .withIcon(method.getIcon(0))


    val params = method.parameterList.parameters.joinToString(", ", "(", ")") {
        it.name + " as " + ZenType.fromJavaType(it.type).displayName
    }

    builder = builder.withTailText(params, true)
        .withInsertHandler(MethodParenthesesHandler(method, false))
        .withTypeText(ZenType.fromJavaType(method.returnType).displayName)
    return builder
}


fun PsiMethod.createGetterFieldLookupElement(name: String): LookupElementBuilder {

    val javaType = this.returnType
    var builder = LookupElementBuilder
        .create(this, name)
        .withIcon(PlatformIcons.PROPERTY_ICON)
    if (javaType is PsiClassType) {
        if (isFunctionalInterface(javaType.resolve())) {
            // TODO
        }
    }
    builder = builder
        .appendTailText(" (From ${this.name}())", true)
        .withTypeText(ZenType.fromJavaType(javaType).displayName, true)
    return builder
}

fun PsiField.createLookupElement(name: String): LookupElementBuilder {

    val javaType = this.type
    var builder = LookupElementBuilder
        .create(this, name)
        .withIcon(PlatformIcons.PROPERTY_ICON)
        .withTypeText(ZenType.fromJavaType(javaType).displayName, true)

    if (javaType is PsiClassType) {
        if (isFunctionalInterface(javaType.resolve())) {
            // TODO
        }
    }

    return builder
}

fun ZenScriptPropertyMember.createLookupElement(name: String): LookupElementBuilder {
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
