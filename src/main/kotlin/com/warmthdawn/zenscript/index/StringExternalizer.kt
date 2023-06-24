package com.warmthdawn.zenscript.index

import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.IOUtil
import java.io.DataInput
import java.io.DataOutput

object StringExternalizer : DataExternalizer<String?> {
    override fun save(out: DataOutput, value: String?) {
        IOUtil.writeString(value, out)
    }

    override fun read(input: DataInput): String? {
        return IOUtil.readString(input)
    }
}