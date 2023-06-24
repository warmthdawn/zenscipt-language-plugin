package com.warmthdawn.zenscript.index

import com.intellij.ide.highlighter.JavaClassFileType
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.*
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import com.warmthdawn.zenscript.ZSLanguageFileType
import com.warmthdawn.zenscript.psi.ZenScriptFile
import org.jetbrains.org.objectweb.asm.AnnotationVisitor
import org.jetbrains.org.objectweb.asm.ClassReader
import org.jetbrains.org.objectweb.asm.ClassVisitor
import org.jetbrains.org.objectweb.asm.Opcodes
import org.jetbrains.org.objectweb.asm.Type
class ZenScriptClassNameIndex : FileBasedIndexExtension<String, String?>() {
    companion object {
        val NAME = ID.create<String, String?>("ZenScriptClassNameIndex")
    }

    override fun getName(): ID<String, String?> = NAME

    override fun getIndexer() = DataIndexer<String, String?, FileContent> { content ->
        if (content.fileType == ZSLanguageFileType) {
            val zsFile = (content.psiFile as ZenScriptFile).scriptBody ?: return@DataIndexer emptyMap()
            val namespace = (content.psiFile as ZenScriptFile).packageName
            zsFile.classes.asSequence()
                .map { namespace + "." + it.name }
                .map { it to null }
                .toMap()
        } else if (content.fileType == JavaClassFileType.INSTANCE) {
            val zenClass = indexJavaClass(content.content)
            if (zenClass != null)
                mapOf(zenClass)
            else
                emptyMap()
        } else {
            emptyMap()
        }

    }

    private fun indexJavaClass(content: ByteArray): Pair<String, String?>? {
        if (content.isEmpty()) {
            return null
        }
        var registered = false
        var className: String? = null
        var javaName: String? = null
        ClassReader(content).accept(object : ClassVisitor(Opcodes.API_VERSION) {
            override fun visit(
                version: Int,
                access: Int,
                name: String,
                signature: String?,
                superName: String?,
                interfaces: Array<out String>?
            ) {
                javaName = name
            }

            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
                if (descriptor == "Lcrafttweaker/annotations/ZenRegister;") {
                    registered = true
                    return null
                }
                if (descriptor == "Lstanhebben/zenscript/annotations/ZenClass;") {
                    return object : AnnotationVisitor(Opcodes.API_VERSION) {
                        override fun visit(name: String?, value: Any?) {
                            if (name == "value") {
                                className = value as? String
                            }
                        }
                    }
                }
                return null
            }
        }, ClassReader.SKIP_CODE)

        return if (registered && className != null) {
            return className!! to Type.getObjectType(javaName).className?.replace('$', '.')
        } else {
            null
        }
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE
    override fun getValueExternalizer(): DataExternalizer<String?> = StringExternalizer

    override fun getVersion(): Int = 12

    override fun getInputFilter(): FileBasedIndex.InputFilter =
        object : DefaultFileTypeSpecificInputFilter(ZSLanguageFileType, JavaClassFileType.INSTANCE) {
            override fun acceptInput(file: VirtualFile): Boolean {
                return !file.name.endsWith(".d.zs")
            }
        }

    override fun dependsOnFileContent(): Boolean = true
}