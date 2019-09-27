package com.licrafter.mc.level.listeners

import com.licrafter.lib.log.BLog
import com.licrafter.mc.item.ItemManager
import com.licrafter.mc.level.*
import com.licrafter.mc.level.events.UWLevelChangedEvent
import com.licrafter.mc.level.events.UWLevelUpEvent
import com.licrafter.mc.level.guis.AltarGui
import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.models.PlayerManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class GuiListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAltarGuiClick(event: InventoryClickEvent) {
        val clickInv = event.clickedInventory ?: return
        val player = event.whoClicked as Player
        val invName = player.openInventory.title
        val altarGui = LevelPlugin.altarGuiConfig()
        if (invName != altarGui.title) {
            return
        }
        //打开了魔法祭台&&操作top背包&&是填充区域
        if (invName == altarGui.title && clickInv.size == altarGui.size
                && AltarGui.isFiller(event.rawSlot, altarGui.size)) {
            event.isCancelled = true
        }
        //右键升级按钮
        if (AltarGui.isButtonFiller(event.rawSlot, altarGui.size) && event.click == ClickType.LEFT) {
            val levelPlayer = PlayerManager.getLevelPlayer(player)
            val level = LevelManager.config.getLevel(levelPlayer?.level ?: -1)
            if (levelPlayer == null || level == null) {
                player.sendMessage("&4你还没有加入魔法升级之路")
                return
            }
            if (LevelManager.config.isMaxLevel(level)) {
                player.sendMessage("&4你已经到达了最高等级")
                return
            }
            val nextLevel = LevelManager.config.getNextLevel(level) ?: return
            val condition = level.condition ?: return

            //检查物品
            //放入的物品名和数量
            val putItemMap = HashMap<String, Int>()
            //需要的物品名和数量
            val needItemMap = HashMap<String, Int>()
            for (index in 0 until event.inventory.size) {
                if (AltarGui.isFiller(index, altarGui.size)) {
                    continue
                }
                val item = event.inventory.getItem(index) ?: continue
                val itemmeta = item.itemMeta ?: continue
                val tagValue = itemmeta.lore?.last() ?: return
                putItemMap[tagValue] = (putItemMap[tagValue] ?: 0).plus(item.amount)
            }
            var canLevelUp = true
            condition.getItemAmountMap().forEach { needItem, needAmount ->
                needItemMap[needItem] = needAmount
                if (putItemMap[needItem] ?: 0 < needAmount) {
                    canLevelUp = false
                }
            }
            if (!canLevelUp) {
                player.sendMessage("条件不满足，无法升级")
                return
            } else {
                //发射Level Up Event
                val levelUpEvent = UWLevelUpEvent(levelPlayer, level, nextLevel)
                Bukkit.getServer().pluginManager.callEvent(levelUpEvent)
                if (levelUpEvent.isCancelled) {
                    BLog.info(LevelPlugin.instance(), "魔法升级被取消")
                    player.closeInventory()
                    return
                }
                for (index in 0 until event.inventory.size) {
                    if (AltarGui.isFiller(index, altarGui.size)) {
                        continue
                    }
                    val item = event.inventory.getItem(index) ?: continue
                    val itemmeta = item.itemMeta ?: continue
                    val tagValue = itemmeta.lore?.last() ?: return
                    val needAmount = needItemMap[tagValue] ?: continue
                    when {
                        needAmount > item.amount -> {
                            needItemMap[tagValue] = needAmount - item.amount
                            event.inventory.setItem(index, null)
                        }
                        needAmount < item.amount -> {
                            needItemMap.remove(tagValue)
                            item.amount -= needAmount
                        }
                        else -> {
                            needItemMap.remove(tagValue)
                            event.inventory.setItem(index, null)
                        }
                    }
                }
                player.closeInventory()
            }

            levelPlayer.level += 1
            player.sendMessage("升级成功!")
            val changeEvent = UWLevelChangedEvent(levelPlayer)
            Bukkit.getServer().pluginManager.callEvent(changeEvent)
        }
    }

    @EventHandler
    fun onSkillGuiClick(event: InventoryClickEvent) {
        val clickInv = event.clickedInventory ?: return
        val player = event.whoClicked as Player
        val invName = player.openInventory.title

        if (!invName.contains("技能选择")) {
            return
        }

        val item = clickInv.getItem(event.rawSlot) ?: return
        if (item.type == Material.AIR) {
            return
        }
        val bindSkill = ItemManager.getSkill(item) ?: return
        SkillsManager.bind(player, bindSkill)
        event.isCancelled = true
        player.closeInventory()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAltarGuiClose(event: InventoryCloseEvent) {
        val altarGui = LevelPlugin.altarGuiConfig()
        val player = event.player
        if (event.view.title == altarGui.title) {
            var droped = false
            for (index in 0 until event.inventory.size) {
                if (AltarGui.isFiller(index, altarGui.size)) {
                    continue
                }
                val item = event.inventory.getItem(index) ?: continue
                if (item.type != Material.AIR) {
                    if (player.inventory.firstEmpty() != -1) {
                        player.inventory.addItem(item)
                    } else {
                        droped = true
                        player.world.dropItem(player.location, item)
                        event.inventory.setItem(index, null)
                    }
                    event.inventory.setItem(index, null)
                }
            }
            if (droped) {
                player.sendMessage("背包空间不足，已经掉落")
                player.world.playSound(player.location, Sound.ENTITY_ENDERMAN_AMBIENT, 1.0F, 1.0F)
            }
        }
    }
}