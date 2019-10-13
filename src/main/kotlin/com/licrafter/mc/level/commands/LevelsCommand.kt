package com.licrafter.mc.level.commands

import com.licrafter.mc.level.models.PlayerManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class LevelsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args[0].equals("guis", ignoreCase = true)) {
            return GuiCmd.perform(sender, command, label, args)
        } else if (args[0].equals("join", ignoreCase = true)) {
            return JoinCmd.perform(sender, command, label, args)
        } else if (args[0].equals("particles", ignoreCase = true)) {
            return ParticleCmd.perform(sender, command, label, args)
        } else if (args[0] == "skill") {
            (sender as Player).inventory.setItemInMainHand(PlayerManager.getLevelPlayer(sender)?.rpgRune?.first())
        } else if (args[0] == "save") {
            //LevelPlugin.effectManager().display(Particle.SMOKE_LARGE,)
            val mutableList = ArrayList<ItemStack>()
            mutableList.add((sender as Player).inventory.itemInMainHand)
            PlayerManager.getLevelPlayer(sender)?.rpgRune = mutableList
            PlayerManager.savePlayers()
        } else if (args[0] == "item" || args[0] == "book") {
            return ItemCmd.perform(sender, command, label, args)
        } else if (args[0] == "bind") {
            return BindCmd.perform(sender, command, label, args)
        } else if (args[0] == "admin") {
            return AdminCmd.perform(sender, command, label, args)
        }
        return true
    }
}