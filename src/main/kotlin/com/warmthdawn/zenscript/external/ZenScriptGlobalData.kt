package com.warmthdawn.zenscript.external

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.warmthdawn.zenscript.index.ZenScriptMemberCache
import com.warmthdawn.zenscript.type.findStaticMembers

class ZenScriptGlobalData(val project: Project) {
    companion object {
        fun getInstance(project: Project): ZenScriptGlobalData {
            return project.getService(ZenScriptGlobalData::class.java)
        }
    }
    private val memberCache = ZenScriptMemberCache(project)
    private val builtinGlobalMethods get() = mapOf(
        "print" to memberCache.getNativeMember("crafttweaker.runtime.GlobalFunctions", "print", true),
        "totalActions" to memberCache.getNativeMember("crafttweaker.runtime.GlobalFunctions", "totalActions", true),
        "enableDebug" to memberCache.getNativeMember("crafttweaker.runtime.GlobalFunctions", "enableDebug", true),
        "isNull" to memberCache.getNativeMember("crafttweaker.runtime.GlobalFunctions", "isNull", true),

        "max" to memberCache.getNativeMethod("java.lang.Math", "max", "int", "int"),
        "min" to memberCache.getNativeMethod("java.lang.Math", "max", "int", "int"),
        "pow" to memberCache.getNativeMethod("java.lang.Math", "pow", "double", "double"),
    ).filter { it.value.isNotEmpty() }
    private val builtinGlobalFields get() = mapOf(

        "logger" to memberCache.getNativeMethod("crafttweaker.CraftTweakerAPI", "getLogger"),
        "recipes" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "recipes", false),
        "furnace" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "furnace", false),
        "oreDict" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "oreDict", false),
        "events" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "events", false),
        "server" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "server", false),
        "client" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "client", false),
        "game" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "game", false),
        "loadedMods" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "loadedMods", false),
        "format" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "format", false),
        "vanilla" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "vanilla", false),
        "itemUtils" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "itemUtils", false),
        "brewing" to memberCache.getNativeMember("crafttweaker.CraftTweakerAPI", "brewingManager", false),
    ).filter { it.value.isNotEmpty() }


    // TODO read logs


    fun getGlobalFunctions(): Map<String, List<PsiElement>> {
        return builtinGlobalMethods
    }
    fun getGlobalFields(): Map<String, List<PsiElement>> {
        return builtinGlobalFields
    }

}