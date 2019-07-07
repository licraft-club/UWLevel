package com.licrafter.mc.skills

import com.licrafter.mc.skills.base.adapter.SkillDefaultAdapter
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class ProjectileExplodeAdapter : SkillDefaultAdapter() {

    override fun onStart(): Boolean {
        val skillParams = getSkillParams() ?: return false
        var explodeCenter = skillParams.projectileTargetEntity?.let { it.location }
                ?: skillParams.projectileTargetBlock?.let { it.location } ?: return false
        val targetEntities = skillParams.projectileTargetEntity?.getNearbyEntities(3.0, 3.0, 3.0)
                ?: skillParams.projectileTargetBlock?.let { it.world.getNearbyEntities(it.location, 3.0, 3.0, 3.0) }
                ?: return false
        skillParams.projectileTargetEntity?.let { targetEntities.add(it) }
        val maxDamage = 5.0
        targetEntities.filter { it is LivingEntity && it !is Player }
                .forEach { mob ->
                    if (mob.isDead) return@forEach
                    //damage
                    doDamageAction(mob as LivingEntity, maxDamage, false)
                    //push
                    doPushAction(mob, explodeCenter)
                }

        return super.onStart()
    }

    private fun doDamageAction(mob: LivingEntity, damage: Double, trueDamage: Boolean) {
        val skillParams = getSkillParams() ?: return
        if (trueDamage) {
            UWSkill.trueDamage(skillParams.mage, mob, damage, skillParams.skill)
        } else {
            UWSkill.damage(skillParams.mage, skillParams.skill, mob, damage)
        }
    }

    private fun doPushAction(mob: Entity, center: Location) {
        var vector = mob.location.subtract(center).toVector()
        if (vector.lengthSquared() == 0.0){
            vector = center.toVector().normalize().multiply(-1)
        }
        vector.multiply(1 / vector.lengthSquared())
        vector.y = vector.y / 5 + 0.5
        mob.velocity = vector
    }
}