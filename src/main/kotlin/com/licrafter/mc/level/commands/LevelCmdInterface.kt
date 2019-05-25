package com.licrafter.mc.level.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
interface LevelCmdInterface {
    abstract fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean
}