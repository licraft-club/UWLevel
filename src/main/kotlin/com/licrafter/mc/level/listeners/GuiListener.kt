package com.licrafter.mc.level.listeners

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.gui.AltarGui
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class GuiListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onAltarGuiClick(event: InventoryClickEvent) {
        val clickInv = event.clickedInventory ?: return
        val player = event.whoClicked as Player
        val invName = player.openInventory.title
        val altarGui = LevelPlugin.levelConfig().altarGui
        if (altarGui == null) {
            BLog.consoleMessage("&c没有魔法祭坛的配置")
            return
        }
        //打开了魔法祭台&&操作top背包&&是填充区域
        if (invName == altarGui.title && clickInv.size == altarGui.size
                && AltarGui.isFiller(event.rawSlot, altarGui.size)) {
            event.isCancelled = true
        }
    }
}