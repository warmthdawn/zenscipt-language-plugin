package com.warmthdawn.zenscript.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import com.warmthdawn.zenscript.ZSLanguage
import com.warmthdawn.zenscript.ZSLanguageFileType

class ZenScriptFile(viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, ZSLanguage) {
    override fun toString(): String {
        return "ZenScript File"
    }
    override fun getFileType(): FileType  = ZSLanguageFileType


    val importList = PsiTreeUtil.getChildOfType(this, ZenScriptImportList::class.java)
    val scriptBody = PsiTreeUtil.getChildOfType(this, ZenScriptScriptBody::class.java)
}