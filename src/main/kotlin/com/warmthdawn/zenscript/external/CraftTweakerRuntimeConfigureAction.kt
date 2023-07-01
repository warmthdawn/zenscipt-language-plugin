package com.warmthdawn.zenscript.external

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

class CraftTweakerRuntimeConfigureAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = runReadAction {
            var fileName = ""
            FilenameIndex.processFilesByName("crafttweaker.log", true, GlobalSearchScope.projectScope(e.project!!)) {
                fileName = it.path
                false
            }
            fileName
        }
        CraftTweakerRuntimeConfigureDialog(e.project!!, file).show()
    }
}