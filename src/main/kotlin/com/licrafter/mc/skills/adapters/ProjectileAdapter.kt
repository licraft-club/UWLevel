package com.licrafter.mc.skills.adapters

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.skills.base.adapter.SkillDefaultAdapter
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.util.BlockIterator
import org.bukkit.util.BoundingBox

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class ProjectileAdapter : SkillDefaultAdapter() {

    var blockIterator: BlockIterator? = null

    override fun onStart(): Boolean {
        val skillParams = getSkillParams() ?: return false
        val mage = skillParams.mage.getPlayer() ?: return false
        val sourceLocation = mage.location
        val projectileLocation = mage.location.add(0.0, 1.0, 0.0)
        val targetBlock = findTargetBlock(mage.eyeLocation, skillParams.skillRange.toInt())
        val targetEntity = findTarget(mage)

        skillParams.projectileTargetBlock = targetBlock
        skillParams.projectileTargetEntity = targetEntity

        val targetLocation = targetEntity?.let { getBoxCenterLocation(it.world, it.boundingBox) }
                ?: targetBlock?.let { getBoxCenterLocation(it.world, it.boundingBox) }
        if (targetLocation == null || targetLocation.x == 0.0 && targetLocation.y == 0.0 && targetLocation.z == 0.0) {
            mage.sendMessage("miss target")
            return true
        }
        if (targetLocation.distance(projectileLocation) <= 3) {
            mage.sendMessage("too close!")
            return true
        }
        val direction = targetLocation.toVector().subtract(projectileLocation.toVector()).normalize().multiply(0.5)
        var distance = projectileLocation.distance(targetLocation)
        for (index in 0..256) {
            LevelPlugin.effectManager().display(Particle.FIREWORKS_SPARK, projectileLocation, 0.1f, 0.1f, 0.1f, 0f, 4, 1f, Color.ORANGE, Material.AIR, 0, 48.toDouble(), null)
            LevelPlugin.effectManager().display(Particle.SPELL_MOB, projectileLocation, 0.1f, 0.1f, 0.1f, 0f, 4, 1f, Color.YELLOW, Material.AIR, 0, 48.toDouble(), null)
            projectileLocation.add(direction)
            distance -= 0.5f
            if (distance < 0.5f) {
                break
            }
        }
        val firework = mage.world.spawn(targetLocation, Firework::class.java)
        val fireworkMeta = firework.fireworkMeta
        //用来标记技能触发者
        fireworkMeta.setDisplayName("level_skill")
        fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.BURST)
                .withColor(Color.ORANGE)
                .withColor(Color.YELLOW)
                .build())
        firework.fireworkMeta = fireworkMeta
        firework.detonate()
        mage.spawnParticle(Particle.EXPLOSION_LARGE, targetLocation, 1, 0.3, 1.0, 0.3)
        mage.playSound(sourceLocation, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        getSkillParams()?.mageNearbyEntities?.filter { it is Player }?.forEach {
            (it as Player).playSound(sourceLocation, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        }
        return super.onStart()
    }

    fun getBoxCenterLocation(world: World, boundingBox: BoundingBox): Location {
        return boundingBox.center.toLocation(world)
    }

    fun findTarget(player: Player): Entity? {
        val params = getSkillParams() ?: return null
        for (entity in params.mageNearbyEntities) {
            if (doesPlayerTarget(player, entity, params.skillRange)) {
                return entity
            }
        }
        return null
    }

    private fun findTargetBlock(projectileLocation: Location, range: Int): Block? {
        blockIterator = null
        initBlockInterator(projectileLocation, range)
        if (blockIterator?.hasNext() == true) {
            var block = blockIterator?.next()
            while (block != null) {
                if (block.type != Material.AIR) {
                    return block
                }
                if (blockIterator?.hasNext() == true) {
                    block = blockIterator?.next()
                } else {
                    break
                }
            }
            return null
        } else {
            return null
        }
    }

    private fun initBlockInterator(location: Location, range: Int) {
        if (blockIterator != null) {
            return
        }
        val loc = location.clone()
        if (loc.blockY < 0) {
            loc.y = 0.0
        }
        val maxHeight = (loc.world?.maxHeight ?: 0).toDouble()
        if (loc.blockY > maxHeight) {
            loc.y = maxHeight
        }
        blockIterator = BlockIterator(loc, 0.0, range)
    }

    private fun doesPlayerTarget(p: Player, entity: Entity, range: Double): Boolean {
        //p is your player
        //target is the location the player might target

        //Check if they are in the same world
        if (entity.world != p.world) return false
        val box = entity.boundingBox.rayTrace(p.eyeLocation.toVector(), p.eyeLocation.direction, range)
                ?: return false

        return true
    }
}