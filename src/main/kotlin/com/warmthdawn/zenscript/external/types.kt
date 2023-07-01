package com.warmthdawn.zenscript.external

import javax.swing.Icon

interface IconEntry {
    val icon: Icon
}

interface NameEntry {
    val mainEntryName: String
    val otherEntryName: String
}
interface TooltipEntry {
    val  tooltipText: String
}