package com.licrafter.mc.item

import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.mc.item.events.LoreVariablesParseEvent
import com.licrafter.mc.level.LevelPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object ItemManager : Listener {

    var itemConfig: ItemConfig? = null
    var bookConfig: BookConfig? = null

    fun onEnable(plugin: LevelPlugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)

        val itemMaker = YmlMaker(plugin, "items/items.yml")
        itemMaker.saveDefaultConfig()
        itemConfig = ParserAPI.instance().loadValues(plugin, ItemConfig::class.java)

        val bookMaker = YmlMaker(plugin, "items/books.yml")
        bookMaker.saveDefaultConfig()
        bookConfig = ParserAPI.instance().loadValues(plugin, BookConfig::class.java)

    }

    @EventHandler
    fun onLoreVariablesParse(event: LoreVariablesParseEvent) {
        if (event.key.equals("skillname", true)) {
        }
    }
}