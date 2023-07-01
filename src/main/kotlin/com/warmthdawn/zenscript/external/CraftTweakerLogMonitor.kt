package com.warmthdawn.zenscript.external

import com.intellij.AppTopics
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.progress.ModalTaskOwner.project
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.util.BackgroundTaskUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.RefreshQueue
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.util.io.lastModified
import com.intellij.util.messages.SimpleMessageBusConnection
import com.intellij.vfs.AsyncVfsEventsListener
import java.nio.file.Path


class CraftTweakerLogMonitor(
        val project: Project,
        disposable: Disposable,
) : AsyncFileListener {

    var logFile: Path? = null

    var onUpdate: (data: ExternalData) -> Unit = { }

    init {
        BackgroundTaskUtil.submitTask(disposable) {

            var lastModified = -1L
            val manager = VirtualFileManager.getInstance()
            while (true) {
                ProgressManager.checkCanceled()
                val logFile = this.logFile
                if (logFile != null) {
                    val modified = logFile.lastModified().toMillis()
                    if (modified != lastModified) {
                        lastModified = modified
                        val file = manager.findFileByNioPath(logFile)
                        if (file != null) {
                            onLogFileChange(file)
                        }
                    }
                }
                Thread.sleep(100)
            }
        }
    }

    private fun onLogFileChange(file: VirtualFile) {

        file.inputStream.reader(file.charset)
                .useLines {
                    val data = processLog(it)
                    onUpdate(data)
                }
    }






    override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
        val logFile = this.logFile ?: return null
        for (event in events) {
            if (event is VFileContentChangeEvent) {
                val file = event.file
                if (file == logFile) {
                    return object : AsyncFileListener.ChangeApplier {
                        override fun afterVfsChange() {
                            runReadAction {
                                if (file.isValid) {
                                    onLogFileChange(file)
                                }
                            }
                        }
                    }
                }
            }
        }
        return null
    }

}