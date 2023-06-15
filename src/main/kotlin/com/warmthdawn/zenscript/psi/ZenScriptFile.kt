package com.warmthdawn.zenscript.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import com.warmthdawn.zenscript.ZSLanguage
import com.warmthdawn.zenscript.ZSLanguageFileType

class ZenScriptFile(viewProvider: FileViewProvider) :
        PsiFileBase(viewProvider, ZSLanguage) {
    override fun toString(): String {
        return "ZenScript File"
    }

    override fun getFileType(): FileType = ZSLanguageFileType


    val importList: ZenScriptImportList = PsiTreeUtil.getChildOfType(this, ZenScriptImportList::class.java)!!
    val scriptBody: ZenScriptScriptBody? = PsiTreeUtil.getChildOfType(this, ZenScriptScriptBody::class.java)

    override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {

        if (lastParent != this.scriptBody) {
            return true
        }
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)

        for (importDeclaration in importList.importDeclarationList) {
            if (!processor.execute(importDeclaration, state)) {
                processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
                return false
            }
        }

        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, null)
        return true
    }


    val packageName: String
        get() {
            val sourceRoot = ProjectRootManager.getInstance(project).fileIndex.getSourceRootForFile(this.virtualFile)?.path

            val currPath = virtualFile.path
            if (sourceRoot == null || !currPath.startsWith(sourceRoot)) {
                return ""
            }

            return "scripts" + currPath.substring(sourceRoot.length, currPath.length - 3).replace('/', '.')
        }

}