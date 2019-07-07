package com.licrafter.mc.skills

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.skills.base.adapter.SkillAdapterFactory
import com.licrafter.mc.skills.base.adapter.SkillRootAdapter
import com.licrafter.mc.skills.base.context.Skill
import com.licrafter.mc.skills.base.context.SkillController
import com.licrafter.mc.skills.base.context.SkillParams
import com.licrafter.mc.skills.event.SkillDamageEvent
import com.licrafter.mc.skills.event.SkillTrueDamageEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
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
            CompatibilityUtils.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', "&b" + p2 + "&7" + p3))
        }, 0, 20)
        val root = SkillAdapterFactory.AdapterChainBuilder()
                .put(SkillRootAdapter())
                .put(ProjectileAdapter())
                .put(ProjectileExplodeAdapter())
                .build() as SkillRootAdapter?
        root?.setSkillController(SkillController(SkillParams(player, Skill())))
        root?.start()
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