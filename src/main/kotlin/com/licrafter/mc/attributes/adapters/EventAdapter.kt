package com.licrafter.mc.attributes.adapters

import com.elmakers.mine.bukkit.utility.CompatibilityUtils
import com.licrafter.mc.attributes.AttributeManager
import com.licrafter.mc.attributes.base.adapter.AttributeDefaultAdapter
import com.licrafter.mc.attributes.base.context.attribute.ability.Health
import com.licrafter.mc.attributes.events.AttributeLoadEvent
import com.licrafter.mc.level.LevelPlugin
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
class EventAdapter : AttributeDefaultAdapter(), Listener {

    override fun onAttch(plugin: LevelPlugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
        super.onAttch(plugin)
    }

    @EventHandler
    fun onAttributeLoad(event: AttributeLoadEvent) {
        scalePlayerMaxHealth(event.player, event.userData.magicHealth)
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
        Bukkit.getScheduler().runTaskLaterAsynchronously(LevelPlugin.instance(), Runnable {
            val attributeData = AttributeManager.mPlayerCache[player.uniqueId] ?: return@Runnable
            val healthAttr = attributeData.getAbility(Health::class.java)

            healthAttr?.let { attr ->
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = attr.getAttrValue().toDouble()
                if (oldHealth != 0.0) {
                    player.health = oldHealth
                }
                player.healthScale = 20.0
                player.isHealthScaled = true
                CompatibilityUtils.sendTitle(player, "", "魔法血条初始完毕!!,血量${player.health}", -1, -1, -1)
            } ?: run {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
            }
        }, 20)
    }
}