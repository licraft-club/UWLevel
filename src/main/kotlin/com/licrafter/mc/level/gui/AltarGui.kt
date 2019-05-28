package com.licrafter.mc.level.gui

import com.licrafter.mc.level.LevelPlugin
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

    fun createAltarGui(player: Player): Inventory {
        val altarGui = LevelPlugin.levelConfig().altarGui
        val size = altarGui!!.size
        val title = altarGui.title
        val filler = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        val buttonFiller = ItemStack(Material.ORANGE_STAINED_GLASS_PANE)
        setupButtonFiller(buttonFiller, player)

        val inventory = Bukkit.createInventory(null, size, title!!)
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

        val levelPlayer = LevelPlugin.playerManager().getLevelPlayer(player)
        val itemMeta = buttonFiller.itemMeta ?: return
        val defaultLores = LevelPlugin.levelConfig().altarGui!!.upgradeLores
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', LevelPlugin.levelConfig().altarGui!!.upgradeDisplay ?: ""))

        val lores = ArrayList<String>()
        if (levelPlayer == null) {
            lores.add(LevelPlugin.langConfig().noPlayerCache)
        } else {
            val level = levelPlayer.getLevel()
            val nextLevel = LevelPlugin.levelConfig().getNextLevel(level!!)
            //祭坛默认lores
            defaultLores.forEach {
                lores.add(ChatColor.translateAlternateColorCodes('&', it.replace("{player}", player.displayName, true).
                        replace("{level}", level.fullname ?: "", true).
                        replace("{nextlevel}", nextLevel?.fullname ?: "已经是最大等级", true)))
            }
            //不同等级额外lores
            level.condition?.let { condition ->
                condition.lores.forEach { lore ->
                    lores.add(ChatColor.translateAlternateColorCodes('&', lore.replace("{mobkill}", condition.mobkill.toString(), true).
                            replace("{money}", condition.money.toString(), true)))
                }
            }
        }

        itemMeta.lore = lores
        buttonFiller.itemMeta = itemMeta
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