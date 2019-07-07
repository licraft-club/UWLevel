package com.licrafter.mc.skills.base.context

import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillParams(val mage: Player, val skill: Skill) {

    var skillRange = 10.0

    val mageNearbyEntities = arrayListOf<Entity>()
    var projectileTargetEntity: Entity? = null
    var projectileTargetBlock: Block? = null

    init {
        mageNearbyEntities.addAll(mage.getNearbyEntities(skillRange, skillRange, skillRange))
    }

    fun release() {
        mageNearbyEntities.clear()
    }
}