package com.licrafter.mc.attribute.adapters

import com.elmakers.mine.bukkit.utility.CompatibilityUtils
import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.attribute.base.adapter.AttributeDefaultAdapter
import com.licrafter.mc.attribute.base.context.abilities.Health
import com.licrafter.mc.attribute.events.PlayerAttributeLoadedEvent
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.Message
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerRespawnEvent

/**
 * Created by shell on 2019/9/21.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class HealthAdapter : AttributeDefaultAdapter(), Listener {

    override fun onAttch(plugin: LevelPlugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
        super.onAttch(plugin)
    }

    @EventHandler
    fun onAttributeLoad(event: PlayerAttributeLoadedEvent) {
        event.userData?.let {
            scalePlayerMaxHealth(event.player, it.magicHealth)
        }
        Message.sendMessage(event.player, "RPG属性已经重载")
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        scalePlayerMaxHealth(event.player, 0.0)
    }

    @EventHandler
    fun onPlayerChangeWorld(event: PlayerChangedWorldEvent) {
        scalePlayerMaxHealth(event.player, event.player.health)
    }

    private fun scalePlayerMaxHealth(player: Player, oldHealth: Double) {
        val attributeData = AttributeManager.mPlayerCache[player.uniqueId] ?: return
        val maxHealth = attributeData.getPlayerBaseAbility(Health::class.java)?.getValue() ?: return
        if (maxHealth <= 0) {
            return
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(LevelPlugin.instance(), Runnable {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = maxHealth.toDouble()
            if (oldHealth != 0.0) {
                player.health = oldHealth
            }
            player.healthScale = 20.0
            player.isHealthScaled = true
            CompatibilityUtils.sendTitle(player, "", "魔法血条初始完毕!!,血量${player.health}", -1, -1, -1)
        }, 20)
    }
}