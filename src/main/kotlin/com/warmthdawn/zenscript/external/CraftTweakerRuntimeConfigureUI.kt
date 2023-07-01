package com.warmthdawn.zenscript.external

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.util.minimumWidth
import com.warmthdawn.zenscript.bundle.ZenScriptBundle
import com.warmthdawn.zenscript.external.LogType.*
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseEvent
import javax.swing.JList
import javax.swing.ListSelectionModel
import kotlin.io.path.Path

private val commands = mutableMapOf(
        ItemNames to "/ct names display modid unloc maxstack maxuse maxdamage rarity repaircost damageable repairable creativetabs",
        Liquids to "/ct liquids"
)

class CraftTweakerRuntimeConfigureDialog(
        val project: Project,
        var fileName: String = ""
) : DialogWrapper(project, null, false, IdeModalityType.PROJECT, false) {

    private val monitor: CraftTweakerLogMonitor


    private var selectedItem: CommandsListEntry? = null
    private val model = CollectionListModel<CommandsListEntry>()
    private val entriesModel = CollectionListModel<Any>()
    private var externalData = ExternalData()
    private var centerPanel: DialogPanel? = null


    init {
        title = "Configure Runtime"
        monitor = CraftTweakerLogMonitor(project, this.disposable)
        monitor.onUpdate = ::updateData
        updateData(ExternalData())

        init()
    }


    private fun toggleListener(open: Boolean): Boolean {
        if (!open) {
            monitor.logFile = null
            return false
        }

        val logFile = LocalFileSystem.getInstance().findFileByNioFile(Path(fileName))
        if (logFile == null || logFile.name != "crafttweaker.log") {
            return false
        }

        monitor.logFile = Path(fileName)
        return true
    }


    private data class CommandsListEntry(
            val type: LogType,
            val name: String,
            val loaded: Boolean,
            val childCount: Int,
    )

    private fun updateData(data: ExternalData) {
        val list = mutableListOf<CommandsListEntry>()

        list.add(CommandsListEntry(ItemNames, "Items", data.items.isNotEmpty(), data.items.size))
        list.add(CommandsListEntry(Liquids, "Liquids", data.liquids.isNotEmpty(), data.liquids.size))

        model.replaceAll(list)
        this.externalData = data
        resetFields()
    }

    private fun updateEntries() {
        when (selectedItem?.type) {
            DumpGlobals -> {
                // TODO
            }

            DumpBracketHandlers -> {
                // TODO
            }

            ItemNames -> {
                entriesModel.replaceAll(externalData.items)
            }

            Liquids -> {
                entriesModel.replaceAll(externalData.liquids)
            }

            Foods -> {
                // TODO
            }

            null -> {
                entriesModel.removeAll()
            }
        }
    }

    private fun resetFields() {
        centerPanel?.reset()
    }

    private fun Panel.configurationView() {
        row {
            label("Command: ")
            textField().bindText({ commands[selectedItem?.type] ?: "" }, {})
                    .applyToComponent {
                        isEditable = false
                    }.align(Align.FILL).resizableColumn()
            link("Copy") {
                val selected = selectedItem
                if (selected != null) {
                    val sel = StringSelection(commands[selected.type])
                    Toolkit.getDefaultToolkit().systemClipboard.setContents(sel, sel)
                }
            }
        }

        row {
            entriesList()
        }.resizableRow()


    }

    private fun Row.entriesList() {
        scrollCell(JBList(entriesModel).apply {
            setEmptyText("Select a loaded entry to show details")
            setCellRenderer(object : ColoredListCellRenderer<Any>() {
                private var tooltip = ""
                override fun customizeCellRenderer(list: JList<out Any>, value: Any, index: Int, selected: Boolean, hasFocus: Boolean) {

                    if (value is IconEntry) {
                        icon = value.icon
                    }
                    if (value is TooltipEntry) {
                        tooltip = value.tooltipText
                    }
                    if (value is NameEntry) {
                        append(value.mainEntryName)
                        append(value.otherEntryName, SimpleTextAttributes.GRAYED_ATTRIBUTES, false)
                    } else {
                        append(value.toString())
                    }
                }

                override fun getToolTipText(event: MouseEvent?): String {
                    return tooltip
                }

                override fun getMinimumSize(): Dimension {
                    return computePreferredSize(true)
                }

                override fun getPreferredSize(): Dimension {
                    return computePreferredSize(true)
                }


            })

        }).align(Align.FILL).resizableColumn()
    }

    private fun Row.commandsList() {

        cell(JBList(model).apply {
            disableEmptyText()
            setCellRenderer(object : ColoredListCellRenderer<CommandsListEntry>() {
                private var tooltip = ""
                override fun customizeCellRenderer(list: JList<out CommandsListEntry>, value: CommandsListEntry, index: Int, selected: Boolean, hasFocus: Boolean) {
                    append(value.name)
                    icon = if (value.loaded) {
                        AllIcons.RunConfigurations.TestPassed
                    } else {
                        AllIcons.RunConfigurations.TestSkipped
                    }
                    tooltip = if (value.loaded) {
                        "Loaded: ${value.name}"
                    } else {
                        "Skipped: ${value.name}"
                    }
                }

                override fun getToolTipText(event: MouseEvent?): String {
                    return tooltip
                }
            })
            selectionMode = ListSelectionModel.SINGLE_SELECTION

            addListSelectionListener {
                selectedItem = selectedValue
                updateEntries()
                resetFields()
            }

        })

    }

    override fun createCenterPanel(): DialogPanel {
        return panel {

            row {
                label(ZenScriptBundle.message("ui.message.log.file"))
                textFieldWithBrowseButton(ZenScriptBundle.message("ui.title.select.file"), project)
                        .bindText(::fileName)
                        .align(Align.FILL)
                        .resizableColumn()

                checkBox(ZenScriptBundle.message("ui.message.monitor")).onChanged {
                    applyFields()
                    val selected = it.isSelected
                    val result = toggleListener(selected)
                    if (result != selected) {
                        it.isSelected = result
                    }
                }
            }

            row {
                panel {
                    row {
                        commandsList()
                    }
                }.gap(RightGap.COLUMNS).align(AlignX.LEFT + AlignY.FILL)

                panel {
                    configurationView()
                }.align(Align.FILL).resizableColumn()
            }.resizableRow()


            row {
                button("Analyse") {
                    applyFields()

                }
            }
        }.also {
            centerPanel = it
        }

    }


}