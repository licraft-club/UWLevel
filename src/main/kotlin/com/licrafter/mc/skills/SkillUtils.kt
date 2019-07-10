package com.licrafter.mc.skills

import com.licrafter.mc.skills.base.context.Mage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import java.util.*

object SkillUtils {
    private val skillCoolingTimeBars = HashMap<UUID, BossBar>()

    fun sendPlayerStayTimeProgressbar(mage: Mage) {
        val player = mage.getPlayer() ?: return
        if (skillCoolingTimeBars[player.uniqueId] == null) {
            val barColor = BarColor.valueOf("PURPLE")
            val barStyle = BarStyle.valueOf("SOLID")
            val bossbar = Bukkit.getServer().createBossBar("技能冷却", barColor, barStyle, BarFlag.PLAY_BOSS_MUSIC)
            skillCoolingTimeBars[player.uniqueId] = bossbar
        }
        skillCoolingTimeBars[player.uniqueId]?.addPlayer(player)
        updatePlayerStayTimeProgressBar(mage)
    }

    fun removePlayerStayTimeProgressbar(player: Player) {
        skillCoolingTimeBars[player.uniqueId]?.removeAll()
        skillCoolingTimeBars.remove(player.uniqueId)
    }

    private fun updatePlayerStayTimeProgressBar(mage: Mage) {
        val bossbar = skillCoolingTimeBars[mage.getPlayer()?.uniqueId] ?: return

        val activeSkill = mage.getActivedSkill() ?: return
        val progress = activeSkill.getCoolingProgress()
        if (progress == 0.0) {
            skillCoolingTimeBars[mage.getPlayer()?.uniqueId]?.removeAll()
            return
        } else {
            bossbar.progress = progress
            val barTitle = ChatColor.translateAlternateColorCodes('&',
                    "冷却时间:{time}".replace("{time}", activeSkill.getCoolingTime().toString(), true))
            bossbar.setTitle(barTitle)
        }
    }
}