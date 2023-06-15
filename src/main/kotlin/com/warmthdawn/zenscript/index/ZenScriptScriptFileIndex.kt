package com.warmthdawn.zenscript.index

import com.intellij.ide.highlighter.JavaClassFileType
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.*
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import com.warmthdawn.zenscript.ZSLanguageFileType
import com.warmthdawn.zenscript.psi.ZenScriptFile
import org.jetbrains.org.objectweb.asm.AnnotationVisitor
import org.jetbrains.org.objectweb.asm.ClassReader
import org.jetbrains.org.objectweb.asm.ClassVisitor
import org.jetbrains.org.objectweb.asm.Opcodes
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElementOfType

class ZenScriptScriptFileIndex : ScalarIndexExtension<String>() {
    companion object {
        val NAME = ID.create<String, Void>("ZenScriptClassNameIndex")
    }

    override fun getName(): ID<String, Void> = NAME

    override fun getIndexer() = DataIndexer<String, Void, FileContent> { content ->
        if (content.fileType == ZSLanguageFileType) {
            val namespace = (content.psiFile as ZenScriptFile).packageName
            mapOf(namespace to null)
        } else {
            emptyMap()
        }

    }

    override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE

    override fun getVersion(): Int = 3

    override fun getInputFilter(): FileBasedIndex.InputFilter = object : DefaultFileTypeSpecificInputFilter(ZSLanguageFileType) {
        override fun acceptInput(file: VirtualFile): Boolean {
            return !file.name.endsWith(".d.zs")
        }
    }

    override fun dependsOnFileContent(): Boolean = true
}