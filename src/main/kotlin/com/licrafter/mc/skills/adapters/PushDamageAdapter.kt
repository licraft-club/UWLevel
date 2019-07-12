package com.licrafter.mc.skills.adapters

import com.licrafter.mc.skills.UWSkill
import com.licrafter.mc.skills.base.adapter.SkillDefaultAdapter
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 *
 * If the parent adapter has a target, the explosion center is the target location,
 * otherwise the explosion center is the mage location
 */
class PushDamageAdapter : SkillDefaultAdapter() {

    override fun onStart(): Boolean {
        val skillParams = getSkillParams() ?: return true
        val parentTargets = getParentTarget()
        val skillCenter = if (parentTargets.isEmpty()) {
            skillParams.mage
        } else {
            parentTargets[0]
        }
        val centerLocation: Location
        val targetEntities: MutableList<Entity>

        when (skillCenter) {
            is LivingEntity -> {
                targetEntities = skillCenter.getNearbyEntities(3.0, 3.0, 3.0)
                targetEntities.add(skillCenter)
                centerLocation = skillCenter.location
            }
            is Block -> {
                targetEntities = skillCenter.world.getNearbyEntities(skillCenter.location, 3.0, 3.0, 3.0).toMutableList()
                centerLocation = skillCenter.location
            }
            else -> return true
        }

        val maxDamage = 5.0
        targetEntities.filter { it is LivingEntity && it !is Player }
                .forEach { mob ->
                    if (mob.isDead) return@forEach
                    //damage
                    doDamageAction(mob as LivingEntity, maxDamage, false)
                    //push
                    doPushAction(mob, centerLocation)
                }

        return super.onStart()
    }

    private fun doDamageAction(mob: LivingEntity, damage: Double, trueDamage: Boolean) {
        val skillParams = getSkillParams() ?: return
        val mage = skillParams.mage.getPlayer() ?: return
        if (trueDamage) {
            UWSkill.trueDamage(mage, mob, damage, skillParams.skill)
        } else {
            UWSkill.damage(mage, skillParams.skill, mob, damage)
        }
    }

    private fun doPushAction(mob: Entity, center: Location) {
        var vector = mob.location.subtract(center).toVector()
        if (vector.lengthSquared() == 0.0) {
            vector = center.toVector().normalize().multiply(-1)
        }
        vector.multiply(1 / vector.lengthSquared())
        vector.y = vector.y / 5 + 0.5
        mob.velocity = vector
    }
}