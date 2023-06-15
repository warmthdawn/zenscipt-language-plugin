package com.warmthdawn.zenscript.index

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.*
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import com.warmthdawn.zenscript.ZSLanguageFileType
import com.warmthdawn.zenscript.psi.ZenScriptFile
import com.warmthdawn.zenscript.psi.ZenScriptVariableDeclaration
import com.warmthdawn.zenscript.util.hasGlobalModifier

class ZenScriptGlobalVariableIndex : ScalarIndexExtension<String>() {
    companion object {
        val NAME = ID.create<String, Void>("ZenScriptGlobalVariableIndex")
    }

    override fun getName(): ID<String, Void> = NAME

    override fun getIndexer() = DataIndexer<String, Void, FileContent> { content ->
        val zsFile = (content.psiFile as? ZenScriptFile)?.scriptBody ?: return@DataIndexer emptyMap()

        zsFile.statements.asSequence()
                .filterIsInstance<ZenScriptVariableDeclaration>()
                .filter { it.hasGlobalModifier }
                .map { it.name to null }
                .toMap()
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE;

    override fun getVersion(): Int = 2

    override fun getInputFilter(): FileBasedIndex.InputFilter = object : DefaultFileTypeSpecificInputFilter(ZSLanguageFileType) {
        override fun acceptInput(file: VirtualFile): Boolean {
            return !file.name.endsWith(".d.zs")
        }
    }

    override fun dependsOnFileContent(): Boolean = true
}