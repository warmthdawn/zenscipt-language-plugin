package com.warmthdawn.zenscript.index

import com.intellij.lang.LighterAST
import com.intellij.util.gist.GistManager
import com.intellij.util.io.DataExternalizer
import java.io.DataInput
import java.io.DataOutput

val zenScriptFileGist = GistManager.getInstance().newPsiFileGist("zenscipt.file.basic", 2, ZenScriptFileDataExternalizer()) { file ->
    findZenClassMembers(file.node.lighterAST)
}

fun findZenClassMembers(lighterAST: LighterAST): Any? {
    return null
}

class ZenScriptFileDataExternalizer : DataExternalizer<Any> {
    override fun save(out: DataOutput, value: Any?) {
        TODO("Not yet implemented")
    }

    override fun read(`in`: DataInput): Any {
        TODO("Not yet implemented")
    }

}