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

class ZenScriptTypeExpansionIndex : ScalarIndexExtension<String>() {
    val NAME = ID.create<String, Void>("ZenScriptTypeExpansionIndex")
    override fun getName(): ID<String, Void> = NAME

    override fun getIndexer() = DataIndexer<String, Void, FileContent> { content ->
        if (content.fileType == ZSLanguageFileType) {
            val zsFile = (content.psiFile as ZenScriptFile).scriptBody ?: return@DataIndexer emptyMap()
            zsFile.expandFunctions.asSequence()
                    .mapNotNull { it.expandTarget?.text }
                    .map { it to null }
                    .toMap()
        } else if (content.fileType == JavaClassFileType.INSTANCE) {
            val expandName = indexZenExpansion(content.content)
            if (expandName != null)
                mapOf(expandName to null)
            else
                emptyMap()
        } else {
            emptyMap()
        }

    }

    private fun indexZenExpansion(content: ByteArray): String? {
        if(content.isEmpty()) {
            return null
        }
        var registered = false
        var expandName: String? = null
        ClassReader(content).accept(object : ClassVisitor(Opcodes.API_VERSION) {
            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
                if (descriptor == "Lcrafttweaker/annotations/ZenRegister;") {
                    registered = true
                    return null
                }
                if (descriptor == "Lstanhebben/zenscript/annotations/ZenExpansion;") {
                    return object : AnnotationVisitor(Opcodes.API_VERSION) {
                        override fun visit(name: String?, value: Any?) {
                            if (name == "value") {
                                expandName = value as? String
                            }
                        }
                    }
                }
                return null
            }
        }, ClassReader.SKIP_CODE)

        return if (registered) {
            return expandName
        } else {
            null
        }
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE

    override fun getVersion(): Int = 1

    override fun getInputFilter(): FileBasedIndex.InputFilter = object: DefaultFileTypeSpecificInputFilter(ZSLanguageFileType, JavaFileType.INSTANCE, JavaClassFileType.INSTANCE) {
        override fun acceptInput(file: VirtualFile): Boolean {
            return !file.name.endsWith(".d.zs")
        }
    }

    override fun dependsOnFileContent(): Boolean = true
}