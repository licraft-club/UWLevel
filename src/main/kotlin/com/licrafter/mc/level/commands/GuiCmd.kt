package com.licrafter.mc.level.commands

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.gui.AltarGui
import com.licrafter.mc.level.ItemManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object GuiCmd : LevelCmdInterface {

    override fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val altarGui = AltarGui()
            val inventory = altarGui.createAltarGui(sender)
            if (inventory == null) {
                sender.sendMessage("创建gui失败，请查看配置文件")
                return true
            }
            sender.openInventory(inventory)
            val item = LevelPlugin.itemConfig().bookMap["bk1"] ?: return true
            val book = LevelPlugin.itemConfig().itemMap["item1"] ?: return true
            val itemStack = ItemManager.createBook("bk1", item)
            val itemStack2 = ItemManager.createItem("item1", book)
            sender.inventory.addItem(itemStack)
            sender.inventory.addItem(itemStack2)
        }
        return true
    }
}