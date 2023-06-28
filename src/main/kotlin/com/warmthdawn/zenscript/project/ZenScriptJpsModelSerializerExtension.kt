package com.warmthdawn.zenscript.project

import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension
import org.jetbrains.jps.model.serialization.library.JpsLibraryRootTypeSerializer
import org.jetbrains.jps.model.serialization.module.JpsModuleSourceRootDummyPropertiesSerializer
import org.jetbrains.jps.model.serialization.module.JpsModuleSourceRootPropertiesSerializer

class ZenScriptJpsModelSerializerExtension: JpsModelSerializerExtension() {

    override fun getModuleSourceRootPropertiesSerializers(): MutableList<out JpsModuleSourceRootPropertiesSerializer<*>> {
        return mutableListOf(JpsModuleSourceRootDummyPropertiesSerializer(ZenScriptSourceRootType, "zenscirpt-source"))
    }
}