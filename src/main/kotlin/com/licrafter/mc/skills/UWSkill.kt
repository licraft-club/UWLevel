package com.licrafter.mc.skills

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.skills.base.context.Mage
import com.licrafter.mc.skills.base.context.Skill
import com.licrafter.mc.skills.event.SkillDamageEvent
import com.licrafter.mc.skills.event.SkillTrueDamageEvent
import net.minecraft.server.v1_14_R1.ChatComponentText
import net.minecraft.server.v1_14_R1.ChatMessageType
import net.minecraft.server.v1_14_R1.PacketPlayOutChat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

object UWSkill {

    var timer: BukkitTask? = null

    fun start(player: Player) {
        var startIndex = 0
        val progressStr = "||||||||||||||||||||||||||||||||||"

        timer?.cancel()
        timer = Bukkit.getScheduler().runTaskTimer(LevelPlugin.instance(), Runnable {
            startIndex = Math.min(startIndex + 1, progressStr.length - 1)
            val p2 = progressStr.substring(0, startIndex)
            val p3 = progressStr.subSequence(startIndex, progressStr.length)
            val ccText = ChatComponentText(ChatColor.translateAlternateColorCodes('&', "&b$p2&7$p3"))
            val packet = PacketPlayOutChat(ccText, ChatMessageType.GAME_INFO)
            (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
        }, 0, 20)
        LevelPlugin.skillController().getMage(player)?.getActivitedSkills()?.let {
            it[ProjectileSkill::class.java.simpleName]?.run()
        }
    }

    fun trueDamage(mage: Player, target: LivingEntity, damage: Double, skill: Skill) {
        val event = SkillTrueDamageEvent(mage, target, damage, skill)
        Bukkit.getPluginManager().callEvent(event)
        if (!event.isCancelled && event.damage != 0.0) {
            target.health = Math.max(Math.min(target.health - event.damage, target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.defaultValue
                    ?: 0.0), 0.0)
        }
    }

    fun damage(mage: Player, skill: Skill, target: LivingEntity, damage: Double) {
        val event = SkillDamageEvent(mage, target, damage, skill)
        Bukkit.getPluginManager().callEvent(event)
        if (!event.isCancelled) {
            target.damage(damage, mage)
        }
    }
}