package com.warmthdawn.zenscript.project

import com.intellij.ide.JavaUiBundle
import com.intellij.ide.projectView.actions.MarkRootActionBase
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectBundle
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.JavaSdkType
import com.intellij.openapi.projectRoots.SdkTypeId
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.roots.libraries.ui.impl.RootDetectionUtil
import com.intellij.openapi.roots.ui.configuration.SdkPopupFactory
import com.intellij.openapi.roots.ui.configuration.libraryEditor.DefaultLibraryRootsComponentDescriptor
import com.intellij.openapi.roots.ui.configuration.projectRoot.LibrariesContainer
import com.intellij.openapi.roots.ui.configuration.projectRoot.LibrariesContainerFactory
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.vfs.JarFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileSystem
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import com.intellij.util.indexing.FileBasedIndex
import com.warmthdawn.zenscript.ZSLanguage
import com.warmthdawn.zenscript.ZSLanguageFileType
import com.warmthdawn.zenscript.index.ZenScriptScriptFileIndex
import com.warmthdawn.zenscript.psi.ZenScriptFile
import kotlinx.serialization.json.*
import java.util.function.Function
import javax.swing.JComponent

class ZenScriptProjectNotification : EditorNotificationProvider, DumbAware {

    override fun collectNotificationData(
        project: Project,
        file: VirtualFile
    ): Function<in FileEditor, out JComponent?> {
        if (!isZenScriptFile(project, file)) {
            return EditorNotificationProvider.CONST_NULL
        }

        val psiFile = PsiManager.getInstance(project).findFile(file) as? ZenScriptFile
            ?: return EditorNotificationProvider.CONST_NULL
        if (psiFile.language !== ZSLanguage) {
            return EditorNotificationProvider.CONST_NULL
        }

        val module = ModuleUtilCore.findModuleForPsiElement(psiFile) ?: return EditorNotificationProvider.CONST_NULL

        if (ModuleRootManager.getInstance(module).sdk.let { it == null || it.sdkType !is JavaSdk }) {
            return createSetupSdkPanel(project, psiFile)
        }

        val sourceRoot = ProjectRootManager.getInstance(project).fileIndex.getSourceRootForFile(file)

        if (sourceRoot == null || sourceRoot.name != "scripts") {
            return createSetupSourceRootPanel(module, file)
        }

        val libraries =
            ModuleRootManager.getInstance(module).orderEntries().librariesOnly().roots(OrderRootType.CLASSES).roots

        if (!libraries.any { isCraftTweakerLib(it) }) {
            return createSetupModsPanel(module, file)
        }


        // lib


        return EditorNotificationProvider.CONST_NULL
    }


    companion object {

        private fun isZenScriptFile(project: Project, file: VirtualFile): Boolean {
            return !(file.extension != ZSLanguageFileType.defaultExtension && !FileTypeRegistry.getInstance()
                .isFileOfType(file, ZSLanguageFileType))
        }

        private fun isCraftTweakerLib(lib: VirtualFile): Boolean {
            val mcmodInfo = lib.findChild("mcmod.info")
                ?: return false
            try {
                val content = mcmodInfo.contentsToByteArray().decodeToString()
                val json = Json.parseToJsonElement(content)
                val isCrt = json.jsonArray[0].jsonObject["modid"]!!.jsonPrimitive.content == "crafttweaker"
                if (isCrt) {
                    return true
                }
            } catch (e: Exception) {
                return false
            }
            return false
        }

        private fun findSourceRoot(file: VirtualFile): VirtualFile? {
            var dir = file.parent
            while (dir != null) {
                if (dir.name == "scripts") {
                    return dir
                }

                dir = dir.parent
            }
            return null
        }

        private fun findMods(scriptsFolder: VirtualFile): List<VirtualFile> {
            val parent = scriptsFolder.parent ?: return emptyList()
            val mods = parent.findChild("mods") ?: return emptyList()

            val result = mutableListOf<VirtualFile>()
            for (child in mods.children) {
                if (child.extension != "jar") {
                    continue
                }

                val jar = JarFileSystem.getInstance().getJarRootForLocalFile(child) ?: continue
                if (jar.findChild("mcmod.info") != null) {
                    result.add(jar)
                }
            }
            return result
        }


        private fun createSetupModsPanel(
            module: Module,
            file: VirtualFile
        ): Function<in FileEditor, out JComponent?> =
            Function { fileEditor: FileEditor ->
                EditorNotificationPanel(fileEditor).apply {
                    text = "Could not find CraftTweaker libraries"
                    createActionLabel("Setup mods libraries") {
                        runWriteAction {
                            val model = ModuleRootManager.getInstance(module).modifiableModel
                            val factory = LibrariesContainerFactory.createContainer(model)
                            val sourceRootFile = findSourceRoot(file)
                            if (sourceRootFile == null) {
                                // TODO: error
                                return@runWriteAction
                            }

                            val jars = findMods(sourceRootFile)
                            if (jars.isEmpty()) {
                                return@runWriteAction
                            }
                            val roots = RootDetectionUtil.detectRoots(
                                jars,
                                null,
                                module.project,
                                DefaultLibraryRootsComponentDescriptor()
                            )
                            val library = factory.createLibrary(
                                factory.suggestUniqueLibraryName("minecraft-mods"),
                                LibrariesContainer.LibraryLevel.MODULE,
                                roots
                            ) ?: return@runWriteAction

                            model.addLibraryEntry(library)
                            model.commit()
                        }
                        module.project.save()
                    }
                }
            }


        private fun createSetupSourceRootPanel(
            module: Module,
            file: VirtualFile
        ): Function<in FileEditor, out JComponent?> =
            Function { fileEditor: FileEditor ->
                EditorNotificationPanel(fileEditor).apply {
                    text = "ZenScript source root (scripts) not defined"
                    createActionLabel("Setup source root") {
                        runWriteAction {
                            val model = ModuleRootManager.getInstance(module).modifiableModel
                            val sourceRootFile = findSourceRoot(file)
                            if (sourceRootFile == null) {
                                // TODO: error
                                return@runWriteAction
                            }

                            val entry = MarkRootActionBase.findContentEntry(model, sourceRootFile)

                            if (entry == null) {
//                                model.addContentEntry()
                                // TODO
                            }

                            if (entry != null) {
                                val sourceFolders = entry.sourceFolders
                                for (sourceFolder in sourceFolders) {
                                    if (Comparing.equal(sourceFolder.file, sourceRootFile)) {
                                        entry.removeSourceFolder(sourceFolder)
                                        break
                                    }
                                }

                                entry.addSourceFolder(sourceRootFile, ZenScriptSourceRootType)
                            }


                            model.commit()
                        }
                        FileBasedIndex.getInstance().requestRebuild(ZenScriptScriptFileIndex.NAME)
                        module.project.save()
                    }
                }
            }


        private fun createSetupSdkPanel(project: Project, file: PsiFile): Function<in FileEditor, out JComponent?> =
            Function { fileEditor: FileEditor ->
                EditorNotificationPanel(fileEditor).apply {
                    text = JavaUiBundle.message("project.sdk.not.defined");
                    createActionLabel(ProjectBundle.message("project.sdk.setup"), SdkPopupFactory.newBuilder()
                        .withProject(project)
                        .withSdkTypeFilter { type: SdkTypeId? -> type is JavaSdkType }
                        .updateSdkForFile(file)
                        .buildEditorNotificationPanelHandler(), true)
                }

            }

    }
}