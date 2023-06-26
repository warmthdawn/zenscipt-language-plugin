package com.warmthdawn.zenscript.misc

import com.intellij.icons.AllIcons
import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.ui.IconManager
import com.intellij.util.PlatformIcons
import com.warmthdawn.zenscript.psi.*
import com.warmthdawn.zenscript.util.hasGlobalModifier
import com.warmthdawn.zenscript.util.hasStaticModifier
import com.warmthdawn.zenscript.util.isReadonly
import javax.swing.Icon

class ZenScriptIconProvider : IconProvider(), DumbAware {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if (element is ZenScriptFile) {
            // TODO
            return null
        }


        return when (element) {
            is ZenScriptVariableDeclaration -> {
                val parent = element.parent
                val isStatic = element.hasStaticModifier
                var icon = if (parent is ZenScriptClassDeclaration) {
                    PlatformIcons.FIELD_ICON
                } else if (parent is ZenScriptScriptBody && (isStatic || element.hasGlobalModifier)) {
                    PlatformIcons.PROPERTY_ICON
                } else {
                    PlatformIcons.VARIABLE_ICON
                }
                if (element.isReadonly) {
                    icon = IconManager.getInstance().createLayered(icon, AllIcons.Nodes.FinalMark)
                }
                if (isStatic) {
                    icon = IconManager.getInstance().createLayered(icon, AllIcons.Nodes.StaticMark)
                }

                icon
            }

            is ZenScriptParameter -> {
                IconManager.getInstance()
                    .createLayered(PlatformIcons.PARAMETER_ICON, AllIcons.Nodes.FinalMark)
            }

            is ZenScriptForeachVariableDeclaration -> {
                IconManager.getInstance()
                    .createLayered(PlatformIcons.VARIABLE_ICON, AllIcons.Nodes.FinalMark)
            }

            is ZenScriptClassDeclaration -> {
                if (element.constructors.isEmpty()) {
                    PlatformIcons.INTERFACE_ICON
                } else {
                    PlatformIcons.CLASS_ICON
                }
            }

            is ZenScriptFunctionDeclaration, is ZenScriptExpandFunctionDeclaration, is ZenScriptConstructorDeclaration -> {
                PlatformIcons.METHOD_ICON
            }

            else -> null
        }
    }
}