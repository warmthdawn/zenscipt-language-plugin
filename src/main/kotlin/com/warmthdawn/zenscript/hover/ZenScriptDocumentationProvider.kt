package com.warmthdawn.zenscript.hover

import com.intellij.codeInsight.javadoc.JavaDocHighlightingManagerImpl
import com.intellij.codeInsight.javadoc.JavaDocInfoGeneratorFactory
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.java.JavaDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiVariable
import com.warmthdawn.zenscript.psi.ZenScriptClass
import com.warmthdawn.zenscript.psi.ZenScriptFile
import com.warmthdawn.zenscript.psi.ZenScriptFunctionDeclaration
import com.warmthdawn.zenscript.psi.ZenScriptIdentifier
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.type.ZenType
import com.warmthdawn.zenscript.type.getType
import com.warmthdawn.zenscript.util.hasGlobalModifier
import com.warmthdawn.zenscript.util.hasStaticModifier
import com.warmthdawn.zenscript.util.isReadonly
import org.jetbrains.annotations.Nls

class ZenScriptDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        var zsElement = element
        if(zsElement is ZenScriptIdentifier) {
            zsElement = element?.parent
        }

        if (zsElement is ZenScriptClass) {
            return generateZenClassInfo(zsElement)
        } else if (zsElement is ZenScriptFunctionDeclaration) {

        } else if (zsElement is ZenScriptVariableDeclaration) {
            return generateVariableInfo(zsElement)
        }else if(element != null) {
            return JavaDocInfoGeneratorFactory.create(element.project, element).generateRenderedDocInfo()
        }

        return null
    }


    private fun generateZenClassInfo(zenClass: ZenScriptClass): String {
        val builder = StringBuilder()

        val packageName = (zenClass.containingFile as ZenScriptFile).packageName

        builder.append(packageName).appendLine()

        builder.append("zenClass").append(" ")

        builder.append(zenClass.name)

        return builder.toString()
    }


    private fun generateVariableInfo(variable: ZenScriptVariableDeclaration): @Nls String? {
        val builder = StringBuilder()
        if (variable.hasGlobalModifier) {
            builder.append("global")
        } else if (variable.hasStaticModifier) {
            builder.append("static")
        } else if (variable.isReadonly) {
            builder.append("val")
        } else {
            builder.append("var")
        }
        builder.append(" ")
        builder.append(variable.name)
        val initializer =  variable.initializer
        val typeRef = variable.typeRef

        if(typeRef == null && initializer == null) {
            return builder.toString()
        }

        builder.append(" ").append("as").append(" ")


        if(typeRef != null) {
            builder.append(ZenType.fromTypeRef(typeRef).displayName)
        } else {
            builder.append(getType(initializer!!.expression).displayName)
        }

        if(initializer != null) {
            builder.append(" ").append("=").append(" ")
            builder.append(initializer.expression.text)
        }

        return builder.toString()
    }
}