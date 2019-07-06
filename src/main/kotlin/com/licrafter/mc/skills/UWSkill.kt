package com.licrafter.mc.skills

import com.licrafter.mc.level.LevelPlugin
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.util.BlockIterator
import org.bukkit.util.BoundingBox

object UWSkill {

    val nearbyEntities = arrayListOf<Entity>()
    var blockIterator: BlockIterator? = null
    val targetRange = 30.0

    fun start(player: Player) {
        val sourceLocation = player.location
        val projectileLocation = player.location.add(0.0, 1.0, 0.0)
        val targetBlock = findTargetBlock(player.eyeLocation)
        nearbyEntities.clear()
        nearbyEntities.addAll(player.getNearbyEntities(targetRange, targetRange, targetRange))
        val targetEntity = findTarget(player)

        val targetLocation = targetEntity?.let { getBoxCenterLocation(it.world, it.boundingBox) }
                ?: targetBlock?.let { getBoxCenterLocation(it.world, it.boundingBox) } ?: return
        if (targetLocation.x == 0.0 && targetLocation.y == 0.0 && targetLocation.z == 0.0) {
            player.sendMessage("miss target")
            return
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
        val firework = player.world.spawn(targetLocation, Firework::class.java)
        val fireworkMeta = firework.fireworkMeta
        fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.BURST)
                .withColor(Color.ORANGE)
                .withColor(Color.YELLOW)
                .build())
        firework.fireworkMeta = fireworkMeta
        firework.detonate()
        player.playSound(sourceLocation, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        nearbyEntities.filter { it is Player }.forEach {
            (it as Player).playSound(sourceLocation, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        }
    }

    fun getBoxCenterLocation(world: World, boundingBox: BoundingBox): Location {
        return boundingBox.center.toLocation(world)
    }

    fun findTarget(player: Player): Entity? {
        for (entity in nearbyEntities) {
            if (doesPlayerTarget(player, entity)) {
                return entity
            }
        }
        return null
    }

    fun findTargetBlock(projectileLocation: Location): Block? {
        blockIterator = null
        initBlockInterator(projectileLocation, targetRange.toInt())
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

    fun initBlockInterator(location: Location, range: Int) {
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

    fun doesPlayerTarget(p: Player, entity: Entity): Boolean {
        //p is your player
        //target is the location the player might target

        //Check if they are in the same world
        if (entity.world?.equals(p.world) == false) return false

        val box = entity.boundingBox.rayTrace(p.eyeLocation.toVector(), p.eyeLocation.direction, targetRange)
                ?: return false

        return true
    }
}