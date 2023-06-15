package com.warmthdawn.zenscript.project

import com.intellij.codeInsight.daemon.ProjectSdkSetupValidator
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectBundle
import com.intellij.openapi.projectRoots.impl.UnknownSdkEditorNotification
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import com.intellij.util.concurrency.annotations.RequiresEdt
import java.util.function.Function
import javax.swing.JComponent

class ZenScriptProjectNotification: EditorNotificationProvider, DumbAware {

    override fun collectNotificationData(project: Project, file: VirtualFile): Function<in FileEditor, out JComponent?> {

        return EditorNotificationProvider.CONST_NULL
    }

    @RequiresEdt
    private fun createPanel(message: @NlsContexts.LinkLabel String,
                            fileEditor: FileEditor,
                            fix: EditorNotificationPanel.ActionHandler): EditorNotificationPanel {
        val panel = EditorNotificationPanel(fileEditor)
        panel.text = message
        panel.createActionLabel(ProjectBundle.message("project.sdk.setup"), fix, true)
        return panel
    }


    private fun checkSourcePath() {

    }
}