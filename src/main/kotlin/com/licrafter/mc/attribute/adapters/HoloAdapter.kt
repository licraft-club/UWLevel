package com.licrafter.mc.attribute.adapters

import com.licrafter.mc.attribute.base.adapter.AttributeDefaultAdapter
import com.licrafter.mc.attribute.display.HoloData
import com.licrafter.mc.attribute.display.NameTagData
import com.licrafter.mc.attribute.events.PlayerCritEntityEvent
import com.licrafter.mc.level.LevelPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by shell on 2019/9/22.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class HoloAdapter : AttributeDefaultAdapter(), Runnable, Listener {

    private var bukkitTask: BukkitTask? = null
    //custom name
    private var nameTagList = hashMapOf<UUID, NameTagData>()
    //crit holo
    private var holoList = ArrayList<HoloData>()

    override fun onAttch(plugin: LevelPlugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
        bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20)
        super.onAttch(plugin)
    }

    override fun run() {
        updateHealProgress()
        updateHoloDisplay()
    }

    override fun onIntercept(): Boolean {
        val entity = (getContext()?.entity ?: return false) as? LivingEntity ?: return false
        val finalDamage = getContext()?.getDamage() ?: 0.0
        onEntityHealthChange(entity, Math.max(0.0, entity.health - finalDamage))
        return super.onIntercept()
    }

    @EventHandler
    fun onRegainHealth(event: EntityRegainHealthEvent) {
        val entity = event.entity as? LivingEntity ?: return
        onEntityHealthChange(entity, entity.health + event.amount)
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (nameTagList.containsKey(event.entity.uniqueId)) {
            updateHealProgress()
        }
    }

    @EventHandler
    fun onPlayerCrit(event: PlayerCritEntityEvent) {
        val holoData = HoloData(event.entity, event.critValue)
        holoList.add(holoData)
    }

    private fun updateHealProgress() {
        val iterator = nameTagList.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.isExpired() || entry.value.entity.isDead) {
                entry.value.entity.customName = entry.value.originCustomName
                iterator.remove()
            } else {
                entry.value.display?.let { display ->
                    entry.value.entity.customName = display
                }
            }
        }
    }

    private fun onEntityHealthChange(entity: LivingEntity, health: Double) {
        var nameTag = nameTagList[entity.uniqueId]
        if (nameTag == null) {
            nameTag = NameTagData(entity)
            nameTagList[entity.uniqueId] = nameTag
        }
        nameTag.setCurentHealth(health)

        nameTag.display?.let {
            entity.customName = it
        }
    }

    private fun updateHoloDisplay() {
        val iterator = holoList.iterator()
        while (iterator.hasNext()) {
            val holoData = iterator.next()
            if (holoData.isCancelled) {
                iterator.remove()
            }
        }
    }

    override fun onRelease() {
        bukkitTask?.cancel()
        holoList.forEach {
            it.cancel()
        }
        super.onRelease()
    }
}