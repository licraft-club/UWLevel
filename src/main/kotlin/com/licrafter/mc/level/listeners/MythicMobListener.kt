package com.licrafter.mc.level.listeners

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

/**
 * Created by shell on 2019/5/28.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class MythicMobListener:Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onMythicMobDeath(event: MythicMobDeathEvent) {
        if (event.mobType.internalName.equals("SkeletalKnight", true)) {
        }
    }
}