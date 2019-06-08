package com.licrafter.mc.level.listeners

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.db.ExecutorCallback
import com.licrafter.mc.level.events.LevelPlayerLoadedEvent
import com.licrafter.mc.level.events.UWLevelChangedEvent
import com.licrafter.mc.level.events.UWLevelUpEvent
import com.licrafter.mc.level.models.LevelPlayer
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import org.fusesource.jansi.Ansi

/**
 * Created by shell on 2019/5/26.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class PlayerListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        //todo filter disable world
        LevelPlugin.dbManager().getRepository()?.getLevelPlayer(player, object : ExecutorCallback<LevelPlayer>() {
            override fun callback(value: LevelPlayer) {
                if (!value.invalidate()) {
                    LevelPlugin.playerManager().addLevelPlayer(value)
                    val loadedEvent = LevelPlayerLoadedEvent(player, value)
                    Bukkit.getServer().pluginManager.callEvent(loadedEvent)
                    BLog.info(LevelPlugin.instance(), "LevelPlayer ${player.displayName} load success!")
                } else {
                    BLog.info(LevelPlugin.instance(), "no level player: ${player.displayName} join game!")
                }
            }
        })
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onMobkill(event: EntityDeathEvent) {
        //todo disable world
        if (event.entity.lastDamageCause !is EntityDamageByEntityEvent) {
            return
        }
        val lastDamageCause = event.entity.lastDamageCause as EntityDamageByEntityEvent

        //extra check for Citizens 2 sentry kills
        if (lastDamageCause.damager is Player && lastDamageCause.damager.hasMetadata("NPC")) {
            return
        }
        //todo mythic mob check

        val damager = lastDamageCause.damager
        val killer = if (damager is Player) {
            damager
        } else if (damager is Projectile && damager.shooter is Player) {
            damager.shooter as Player
        } else {
            return
        }
        val levelPlayer = LevelPlugin.playerManager().getLevelPlayer(killer.uniqueId) ?: return
        levelPlayer.addMobkilled(1)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onCraftItem(event: CraftItemEvent) {
        System.out.println(event.click)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun prepareCraftItem(event: PrepareItemCraftEvent) {
        System.out.println(event.isRepair)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
    }
}