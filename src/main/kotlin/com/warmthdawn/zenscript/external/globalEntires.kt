package com.warmthdawn.zenscript.external

import com.intellij.xml.util.XmlStringUtil


class ExternalData {
    val items = mutableListOf<ItemsEntry>()
    val liquids = mutableListOf<LiquidEntry>()
}

data class GlobalEntry(
        val name: String,
        val isMethod: Boolean,
        val declClazz: String,
        val memberName: String,
        val methodSignatures: List<String>?,
)

data class LiquidEntry(
        val registryName: String,
        val displayName: String,
        val bracketText: String,
        val luminosity: Int,
        val density: Int,
        val temperature: Int,
        val viscosity: Int,
        val gaseous: Boolean
) : NameEntry, TooltipEntry {

    override val mainEntryName = bracketText
    override val otherEntryName: String by lazy {
        "($displayName)"
    }
    override val tooltipText: String by lazy {
        buildString {
            append("<html><body>")

            append("<h2>").append(displayName).append("</h2>")
            append(XmlStringUtil.escapeString(bracketText)).append("<br>")
            append("</body></html>")
        }
    }
}

data class ItemsEntry(
        val registryName: String,
        val bracketText: String,
        val nbt: String?,
        val displayName: String,
        val modId: String,
        val unlocalizedName: String,
        val maxStackSize: Int,
        val maxItemUseDuration: Int,
        val maxItemDamage: Int,
        val rarity: String,
        val repairCost: Int,
        val damageable: Boolean,
        val repairable: Boolean,
        val creativeTabs: List<String>,
) : NameEntry, TooltipEntry {
    override val mainEntryName: String by lazy {
        bracketText.substringBefore(".")
    }
    override val otherEntryName: String by lazy {
        val dotIndex = bracketText.indexOf(".")
        if (dotIndex > 0) {
            bracketText.substring(dotIndex)
        } else {
            ""
        }
    }
    override val tooltipText: String by lazy {
        buildString {
            append("<html><body>")

            append("<h2>").append(displayName).append("</h2>")
            append("&lt;item:").append(registryName).append("&gt;<br>")
            if (nbt != null) {
                append("NBT: ").append(XmlStringUtil.escapeString(nbt)).append("<br>")
            }
            append("<span>").append(modId).append("</span>")
            append("</body></html>")
        }
    }
}

private val registryNameRegex = Regex("<(?<name>.+)>(\\.withTag\\((?<tag>.+)\\))?")
fun ItemsEntry(titles: List<String>, entries: List<String>): ItemsEntry? {

    var registryName: String? = null
    var displayName = ""
    var bracketText = ""
    var nbt: String? = null
    var modId = ""
    var unlocalizedName = ""
    var maxStackSize = 64
    var maxItemUseDuration = 0
    var maxItemDamage = 0
    var rarity = ""
    var repairCost = 0
    var damageable = false
    var repairable = false
    var creativeTabs = emptyList<String>()

    for ((i, value) in entries.withIndex()) {
        when (titles[i]) {
            "REGISTRY_NAME" -> {
                bracketText = value.replace("\"\"", "\"")
                registryNameRegex.matchEntire(value)?.let {
                    registryName = it.groups["name"]?.value
                    nbt = it.groups["tag"]?.value?.replace("\"\"", "\"")
                }
            }

            "DISPLAY_NAME" -> displayName = value
            "MOD_ID" -> modId = value
            "UNLOCALIZED" -> unlocalizedName = value
            "MAX_STACK_SIZE" -> maxStackSize = value.toInt()
            "MAX_ITEM_USE_DURATION" -> maxItemUseDuration = value.toInt()
            "MAX_ITEM_DAMAGE" -> maxItemDamage = value.toInt()
            "RARITY" -> rarity = value
            "REPAIR_COST" -> repairCost = value.toInt()
            "DAMAGEABLE" -> damageable = value.toBoolean()
            "REPAIRABLE" -> repairable = value.toBoolean()
            "CREATIVE_TABS" -> {
                if (value.firstOrNull() == '[' && value.lastOrNull() == ']') {
                    creativeTabs = value.trim('[', ']').split(',').map { it.trim() }
                }
            }
        }
    }
    if (registryName.isNullOrEmpty()) {
        return null
    }

    return ItemsEntry(registryName!!,
            bracketText,
            nbt,
            displayName,
            modId,
            unlocalizedName,
            maxStackSize,
            maxItemUseDuration,
            maxItemDamage,
            rarity,
            repairCost,
            damageable,
            repairable,
            creativeTabs)

}
