package com.licrafter.mc.level.listeners

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.events.UWLevelChangedEvent
import com.licrafter.mc.level.events.UWLevelUpEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

/**
 * Created by shell on 2019/6/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class UWLevelListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onUwLevelUp(event: UWLevelUpEvent) {
        BLog.consoleMessage("event level up")
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onUwLevelChanged(event: UWLevelChangedEvent) {
    }
}