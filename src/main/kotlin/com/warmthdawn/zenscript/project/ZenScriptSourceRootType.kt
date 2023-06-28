package com.warmthdawn.zenscript.project

import org.jdom.Element
import org.jetbrains.jps.model.JpsDummyElement
import org.jetbrains.jps.model.JpsElementFactory
import org.jetbrains.jps.model.ex.JpsElementTypeWithDummyProperties
import org.jetbrains.jps.model.impl.JpsDummyElementImpl
import org.jetbrains.jps.model.library.JpsLibraryType
import org.jetbrains.jps.model.module.JpsModuleSourceRootType
import org.jetbrains.jps.model.serialization.module.JpsModuleSourceRootPropertiesSerializer

object ZenScriptSourceRootType : JpsElementTypeWithDummyProperties(), JpsModuleSourceRootType<JpsDummyElement>
object ZenScriptModsLibraryRootType: JpsElementTypeWithDummyProperties(), JpsLibraryType<JpsDummyElement>



class ZenScriptSourceRootSerializer(type: JpsModuleSourceRootType<JpsDummyElement>?, typeId: String?) : JpsModuleSourceRootPropertiesSerializer<JpsDummyElement>(
    type, typeId
) {
    override fun loadProperties(sourceRootTag: Element): JpsDummyElement {
        return JpsElementFactory.getInstance().createDummyElement()
    }

    override fun saveProperties(properties: JpsDummyElement, sourceRootTag: Element) {

    }

}