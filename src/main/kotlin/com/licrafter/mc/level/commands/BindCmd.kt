package com.licrafter.mc.level.commands

import com.licrafter.mc.level.SkillsManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/8/2.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object BindCmd : LevelCmdInterface {
    override fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage("/levels bind [skill]")
            return true
        }
        if (sender !is Player) {
            sender.sendMessage("必须玩家")
            return true
        }
        SkillsManager.bind(sender, args[1])
        return true
    }
}