package com.warmthdawn.zenscript

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object ZSLanguageFileType : LanguageFileType(ZSLanguage) {
    override fun getName() = "ZenScript File"

    override fun getDescription() = "ZenScript file"

    override fun getDefaultExtension() = "zs"

    override fun getIcon(): Icon = AllIcons.FileTypes.JavaClass
}