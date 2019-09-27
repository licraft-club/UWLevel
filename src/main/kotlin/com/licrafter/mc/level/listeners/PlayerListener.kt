package com.licrafter.mc.level.listeners

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.PlayerManager
import com.licrafter.mc.level.events.LevelPlayerLoadedEvent
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftProjectile
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

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
        val userData = PlayerManager.loadLevelPlayer(player)
        userData?.let {
            val loadEvent = LevelPlayerLoadedEvent(event.player, it)
            Bukkit.getServer().pluginManager.callEvent(loadEvent)
            BLog.consoleMessage("reload success level abilities ${userData.displayName}")
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        PlayerManager.savePlayer(event.player)
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
        } else if (damager is Firework) {
            //skill has firework explode
            try {
                val display = damager.fireworkMeta.displayName
                val mageUUID = UUID.fromString(display)
                LevelPlugin.instance().server.getPlayer(mageUUID) ?: return
            } catch (e: IllegalArgumentException) {
                return
            }
        } else {
            return
        }
        val levelPlayer = PlayerManager.getLevelPlayer(killer.uniqueId) ?: return
        levelPlayer.mobKill += 1
        killer.sendMessage("获得怪物灵魂+" + 1)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onCraftItem(event: CraftItemEvent) {

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun prepareCraftItem(event: PrepareItemCraftEvent) {
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerAttacked(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            player.sendMessage(event.damager.name + "受到伤害: " + Math.floor(event.damage) + " 血量:" + Math.floor(player.health))
        }
        if ((event.damager is Player) && event.entity is Mob) {
            val player = event.damager as Player
            val mob = event.entity as Mob
            player.sendMessage("造成伤害: " + Math.floor(event.damage) + " 血量: " + Math.floor(mob.health))
        }

        if (event.damager is CraftProjectile && event.entity is Mob) {
            val shooter = (event.damager as CraftProjectile).shooter
            val mob = event.entity as Mob
            (shooter as Player).sendMessage("造成伤害: " + Math.floor(event.damage) + " 血量: " + Math.floor(mob.health))
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onLevelSkillFireworkDamage(event: EntityDamageEvent) {
        System.out.println(event.cause.name)
    }
}