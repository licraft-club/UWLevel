package com.licrafter.mc.level.guis

import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.Message
import com.licrafter.mc.level.models.PlayerManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.ArrayList

/**
 * Created by shell on 2019/5/26.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class AltarGui {

    fun createAltarGui(player: Player): Inventory? {
        val altarGui = LevelPlugin.altarGuiConfig()
        val size = altarGui.size
        val title = altarGui.title
        val filler = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        val buttonFiller = ItemStack(Material.ORANGE_STAINED_GLASS_PANE)
        setupButtonFiller(buttonFiller, player)

        val inventory = Bukkit.createInventory(null, size, title)
        for (position in 0 until altarGui.size) {
            if (isFiller(position, altarGui.size)) {
                inventory.setItem(position, filler)
            }
            if (isButtonFiller(position, altarGui.size)) {
                inventory.setItem(position, buttonFiller)
            }
        }
        return inventory
    }

    private fun setupButtonFiller(buttonFiller: ItemStack, player: Player) {

        val levelPlayer = PlayerManager.getLevelPlayer(player)
        val itemMeta = buttonFiller.itemMeta ?: return
        val defaultLores = LevelPlugin.altarGuiConfig().upgradeLores
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', LevelPlugin.altarGuiConfig().upgradeDisplay
                ?: ""))

        val lores = ArrayList<String>()
        if (levelPlayer == null) {
            lores.add(Message.config.noPlayerCache)
        } else {
            val level = LevelManager.config.getLevel(levelPlayer.level)
            val nextLevel = LevelManager.config.getNextLevel(level!!)
            //祭坛默认lores
            defaultLores.forEach {
                val prefix = LevelManager.config.getLevelPrefix(level)?.fullname
                lores.add(ChatColor.translateAlternateColorCodes('&', it.replace("{ability}", player.displayName, true).replace("{level}", prefix
                        ?: "", true)))
            }
            //不同等级额外lores
            level.condition?.apply {
                lores.add(ChatColor.translateAlternateColorCodes('&', "&b消耗金币: &e$money"))
                lores.add(ChatColor.translateAlternateColorCodes('&', "&b消耗怪物灵魂(击杀): &e$mobkill"))
                items.forEach {
                    val itemPair = getItemNameAmountPair(it)
                    val item = LevelPlugin.itemConfig().itemMap[itemPair.first]
                    item?.apply {
                        lores.add(ChatColor.translateAlternateColorCodes('&', "&b消耗魔法物品: &e$display ${itemPair.second} 个"))
                    } ?: apply {
                        val book = LevelPlugin.itemConfig().itemMap[itemPair.first]
                        book?.apply {
                            lores.add(ChatColor.translateAlternateColorCodes('&', "&b消耗魔法书: &e$display ${itemPair.second} 个"))
                        }
                    }
                }
            }
        }

        itemMeta.lore = lores
        buttonFiller.itemMeta = itemMeta
    }

    private fun getItemNameAmountPair(itemStr: String): Pair<String, Int> {
        val array = itemStr.split(",")
        var itemName = array[0]
        var itemCount = 1
        if (array.size > 1) {
            itemCount = try {
                array[1].toInt()
            } catch (e: Exception) {
                1
            }
        }
        return Pair(itemName, itemCount)
    }

    companion object {
        /**
         * 是否为填充区域
         *
         * @param slot
         * @param size
         * @return
         */
        fun isFiller(slot: Int, size: Int): Boolean {
            return slot < 9 || slot % 9 == 0 || slot % 9 == 8 || slot > size - 9
        }

        /**
         * 是否为升级按钮
         *
         * @param slot
         * @param size
         * @return
         */
        fun isButtonFiller(slot: Int, size: Int): Boolean {
            return slot == size - 5
        }
    }
}