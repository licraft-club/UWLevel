package com.licrafter.mc.level

import com.elmakers.mine.bukkit.api.magic.MagicAPI
import com.licrafter.lib.log.BLog
import com.licrafter.mc.item.ItemManager
import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.models.PlayerManager
import com.licrafter.mc.level.tasks.MageTask
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.Configuration
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

object SkillsManager {

    var magicAPI: MagicAPI? = null
        private set
    private var mageTaskRunnable: MageTask? = null
    private var mageTask: BukkitTask? = null


    fun onEnable() {
        val magicPlugin = Bukkit.getPluginManager().getPlugin("Magic")
        if (magicPlugin != null && magicPlugin is MagicAPI) {
            magicAPI = magicPlugin
            mageTaskRunnable = MageTask()
            mageTask = Bukkit.getScheduler().runTaskTimerAsynchronously(LevelPlugin.instance(), mageTaskRunnable!!, 20, 20)
            BLog.info(LevelPlugin.instance(), "mageTask started!")
        }
    }

    fun cast(skill: String, configuration: Configuration?, sender: Player): Boolean {
        return magicAPI?.cast(skill, configuration, sender, sender) ?: false
    }

    fun bind(player: Player, skill: String) {
        val handItem = player.inventory.itemInMainHand
        if (handItem.type == Material.AIR) {
            return
        }
        val id = ItemManager.getId(handItem) ?: return
        if (id != LevelManager.config.wand) {
            return
        }
        val levelPlayer = PlayerManager.getLevelPlayer(player) ?: return
        val skillLevel = levelPlayer.skillMap[skill] ?: return

        val skillTemp = magicAPI?.getSpellTemplate(if (skillLevel.level <= 1) skill else "$skill|$skillLevel")
        if (skillTemp == null) {
            player.sendMessage("无此技能")
            return
        }
        val config = LevelPlugin.itemConfig().itemMap[id] ?: return
        val itemMeta = handItem.itemMeta ?: return
        val bindSkill = "&b${skillTemp.name} ${ItemManager.getRomanValue(skillLevel.level)}"
        itemMeta.lore = config.lores.map {
            if (it.contains("技能:")) {
                ChatColor.translateAlternateColorCodes('&', "&e技能: $bindSkill")
            } else {
                ChatColor.translateAlternateColorCodes('&', it)
            }
        }
        handItem.itemMeta = itemMeta
        ItemManager.setSkill(handItem, skill)
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "成功绑定了技能: $bindSkill"))
    }

    fun onDisable() {
        mageTask?.cancel()
    }
}