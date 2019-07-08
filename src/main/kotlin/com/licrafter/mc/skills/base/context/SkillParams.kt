package com.licrafter.mc.skills.base.context

import org.bukkit.block.Block
import org.bukkit.entity.Entity

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillParams(val mage: Mage, val skill: Skill) {

    var skillRange = 10.0

    val mageNearbyEntities = arrayListOf<Entity>()
    var projectileTargetEntity: Entity? = null
    var projectileTargetBlock: Block? = null

    init {
        mage.getPlayer()?.let {
            mageNearbyEntities.addAll(it.getNearbyEntities(skillRange, skillRange, skillRange))
        }
    }

    fun release() {
        mageNearbyEntities.clear()
    }
}