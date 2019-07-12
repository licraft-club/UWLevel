package com.licrafter.mc.level.commands

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.skills.UWSkill
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class LevelsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args[0].equals("gui", ignoreCase = true)) {
            return GuiCmd.perform(sender, command, label, args)
        } else if (args[0].equals("join", ignoreCase = true)) {
            return JoinCmd.perform(sender, command, label, args)
        } else if (args[0].equals("particles", ignoreCase = true)) {
            return ParticleCmd.perform(sender, command, label, args)
        } else if (args[0] == "skill") {
            UWSkill.start(sender as Player)
        } else if (args[0] == "skillapi") {
            LevelPlugin.skillApiManager().cast(sender as Player)
        }
        return true
    }
}