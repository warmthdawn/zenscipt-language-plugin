package com.warmthdawn.zenscript.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.warmthdawn.zenscript.ZSLanguage
import com.warmthdawn.zenscript.ZSLanguageFileType

class ZenScriptFile(viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, ZSLanguage) {
    override fun toString(): String {
        return "ZenScript File"
    }
    override fun getFileType(): FileType  = ZSLanguageFileType
}