package com.licrafter.mc.level.listeners

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.db.ExecutorCallback
import com.licrafter.mc.level.models.LevelPlayer
import com.licrafter.mc.level.models.ItemManager
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
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
            override fun callback(value: LevelPlayer?) {
                value?.let { levelPlayer ->
                    LevelPlugin.playerManager().addLevelPlayer(levelPlayer)
                } ?: apply {
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
    fun onMythicMobDeath(event: MythicMobDeathEvent) {
        if (event.mobType.internalName.equals("SkeletalKnight", true)) {
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onCraftItem(event: CraftItemEvent) {

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun prepareCraftItem(event: PrepareItemCraftEvent) {

    }
}