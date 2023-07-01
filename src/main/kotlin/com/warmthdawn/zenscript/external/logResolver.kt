package com.warmthdawn.zenscript.external


enum class LogType(val titleText: String) {
    DumpGlobals("Globals:"),
    DumpBracketHandlers("Bracket Handlers:"),
    ItemNames("List of all registered Items:"),
    Liquids("Liquids:"),
    Foods("Foods:"),

    ;

    companion object {
        val allTypes = LogType.values()
        val allTypesMap = allTypes.associateBy { it.titleText }
    }
}


fun matchLogTitle(line: String): LogType? {
    return LogType.allTypesMap.getOrDefault(line, null)
}

private class LogReader(val iterator: Iterator<String>) {
    private var current = iterator.next()
    private var isEof = false

    val data = ExternalData()
    fun current() = current
    fun next(): Boolean {
        if (!iterator.hasNext()) {
            isEof = true
            return false
        }
        current = iterator.next()
        return true
    }

    fun isEof() = isEof

}

///ct names display modid unloc maxstack maxuse maxdamage rarity repaircost damageable repairable creativetabs
fun processLog(lines: Sequence<String>): ExternalData {
    val reader = LogReader(lines.iterator())

    while (!reader.isEof()) {
        readLogEntry(reader)
    }

    return reader.data
}

private val globalsRegex = Regex("^(?<name>[A-Za-z0-9_]+): (?<desc>.+)$")

//private val itemNamesRegex = Regex("^(?:\"((?:([^\"]|(\"\"))*)+)\",?)+$")
private fun readGlobals(reader: LogReader) {
    do {
        val result = globalsRegex.matchEntire(reader.current())
                ?: return
        val (name, desc) = result.destructured

        if (desc.startsWith("SymbolJavaStaticField: ")) {
            val field = desc.substringAfterLast(' ')
            val lastDot = field.lastIndexOf('.')
            val clazz = field.substring(0, lastDot)
            val fieldName = field.substring(lastDot + 1)
            GlobalEntry(name, false, clazz, fieldName, null)
        } else if (desc.startsWith("SymbolJavaStaticMethod: JavaMethod: ")) {
//            val
//            GlobalEntry(name, false, clazz, fieldName, null)
        } else {
            GlobalEntry(name, false, "", "", null)
        }

    } while (reader.next())
}

private fun readLogEntry(reader: LogReader) {
    do {
        val title = matchLogTitle(reader.current())
        if (title != null) {
            if (!reader.next()) {
                return
            }
            when (title) {
//                LogType.DumpGlobals -> readGlobals(reader)
                LogType.Liquids -> {
                    readLiquidNames(reader)
                }

                LogType.ItemNames -> readItemNames(reader)
                else -> {}
            }
            return
        }

    } while (reader.next())

}

private fun matchNext(reader: LogReader, start: String, subString: Boolean = false): String? {
    if (!reader.next()) {
        return null
    }
    val line = reader.current()
    if (line.startsWith(start)) {
        if (subString) {
            return line.substring(start.length)
        }
        return line
    }
    return null
}

private fun readLiquidNames(reader: LogReader) {
    val liquids = reader.data.liquids
    liquids.clear()

    do {
        val line = reader.current()
        if (!line.startsWith("<liquid:")) {
            return
        }
        val registryName = matchNext(reader, "- Name: ", true)
                ?: return
        val displayName = matchNext(reader, "- Display Name: ", true)
                ?: return
        val luminosity = matchNext(reader, "- Luminosity:", true)
                ?: return
        val density = matchNext(reader, "- Density: ", true)
                ?: return
        val temperature = matchNext(reader, "- Temperature: ", true)
                ?: return
        val viscosity = matchNext(reader, "- Viscosity: ", true)
                ?: return
        val gaseous = matchNext(reader, "- Gaseous: ", true)
                ?: return
        liquids.add(LiquidEntry(registryName, displayName, line,
                luminosity.toInt(), density.toInt(), temperature.toInt(), viscosity.toInt(), gaseous.toBoolean()))

    } while (reader.next())

}

private val stringRegex = Regex("\"(?<string>([^\"]|(\"\"))*)\"")
private fun processItemNames(line: String): List<String>? {
    val result = mutableListOf<String>()
    var index = 0
    while (index < line.length) {
        val match = stringRegex.matchAt(line, index)
                ?: return null
        val data = match.groups["string"]!!.value
        index += match.value.length
        if (index < line.length && line[index] != ',') {
            return null
        }
        index++
        result.add(data)
    }
    return result
}

private fun readItemNames(reader: LogReader) {
    val items = reader.data.items
    items.clear()
    val firstLine = reader.current()
    val titles = processItemNames(firstLine)
            ?: return
    while (reader.next()) {
        val line = reader.current()
        val match = processItemNames(line)
                ?: break
        if (match.size != titles.size) {
            break
        }
        ItemsEntry(titles, match)?.let {
            items.add(it)
        }
        reader.next()
    }

}
