package com.licrafter.mc.skills

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.skills.base.context.Skill
import com.licrafter.mc.skills.event.SkillDamageEvent
import com.licrafter.mc.skills.event.SkillTrueDamageEvent
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

object UWSkill {

    fun start(player: Player) {
        LevelPlugin.skillController().getMage(player)?.getActivedSkill()?.let {
            it.run()
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