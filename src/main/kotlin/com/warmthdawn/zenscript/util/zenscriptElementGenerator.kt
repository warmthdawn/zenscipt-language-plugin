package com.warmthdawn.zenscript.util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import com.warmthdawn.zenscript.ZSLanguageFileType
import com.warmthdawn.zenscript.psi.ZenScriptFile
import com.warmthdawn.zenscript.psi.ZenScriptIdentifier
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration


fun createIdentifierFromText(project: Project, name: String): ZenScriptIdentifier? {
    val dummyFile: ZenScriptFile = createFile(project, "var $name;")
    val variableDecl = PsiTreeUtil.getChildOfType(dummyFile.scriptBody, ZenScriptVariableDeclaration::class.java)
    return variableDecl?.identifier
}

fun createFile(project: Project, text: String): ZenScriptFile {
    val name = "dummy.zs"
    return PsiFileFactory.getInstance(project).createFileFromText(name, ZSLanguageFileType, text) as ZenScriptFile
}