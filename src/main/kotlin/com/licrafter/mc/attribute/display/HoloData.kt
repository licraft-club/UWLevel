package com.licrafter.mc.attribute.display

import com.gmail.filoghost.holographicdisplays.`object`.CraftHologram
import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.level.LevelPlugin
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

/**
 * Created by shell on 2019/9/22.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class HoloData(entity: LivingEntity, damage: Double) : BukkitRunnable() {

    private val holoDisplay = AttributeManager.config.holoDamageDisplay
    private var hologram: CraftHologram? = null

    init {
        if (holoDisplay != null) {
            val loc = entity.eyeLocation.clone().add(0.0, Random().nextDouble()+0.5, 0.0)
            loc.yaw = entity.location.yaw - 90
            loc.add(loc.direction.multiply(0.8))
            hologram = CraftHologram(loc)
            hologram?.appendTextLine(String.format(holoDisplay.format, String.format("%.1f", damage)))
            runTaskTimer(LevelPlugin.instance(), 5, 2)
        }
    }

    private var updateTime = System.currentTimeMillis()

    override fun run() {
        val moveDistance = 0.1
        hologram?.let {
            if (isExpired()) {
                it.delete()
            } else {
                it.teleport(it.location.add(0.0, moveDistance, 0.0))
            }
        }
    }

    fun isExpired(): Boolean {
        return (System.currentTimeMillis() - updateTime) >= (holoDisplay?.duration ?: 2) * 1000L
    }
}