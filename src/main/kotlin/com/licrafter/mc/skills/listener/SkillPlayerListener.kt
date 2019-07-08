package com.licrafter.mc.skills.listener

import com.licrafter.mc.skills.UWSkill
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillPlayerListener : Listener {

    @EventHandler
    fun playerholdItem(event: PlayerItemHeldEvent) {
        val newslot = event.newSlot
        System.out.println(newslot)
    }

    @EventHandler
    fun onRightClickHand(event: PlayerInteractEvent) {
        System.out.println(event.action.name)
        UWSkill.start(event.player)
    }

    @EventHandler
    fun onRightClickHand(event: PlayerDropItemEvent) {
        System.out.println(event.player.inventory.itemInMainHand.type)
    }

    @EventHandler
    fun onRightClickHand(event: InventoryClickEvent) {
        val player = (event.whoClicked as Player)
        val item = player.inventory.itemInMainHand
        System.out.println(event.cursor?.type?:"无")
    }
}