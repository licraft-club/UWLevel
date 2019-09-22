package com.licrafter.mc.level.commands

import com.licrafter.mc.level.LevelPlugin
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * Created by shell on 2019/8/4.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object AdminCmd : LevelCmdInterface {
    override fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            return true
        }
        val op = args[1]
        when (op) {
            "reload" -> {
                LevelPlugin.instance().reload()
                sender.sendMessage("reload success")
            }
        }
        return true
    }
}