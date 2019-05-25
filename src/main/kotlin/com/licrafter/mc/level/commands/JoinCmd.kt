package com.licrafter.mc.level.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

object JoinCmd : LevelCmdInterface {
    override fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {

        return true
    }
}