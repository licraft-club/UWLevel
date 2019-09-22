package com.licrafter.mc.level.commands

import com.licrafter.mc.level.models.PlayerManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/6/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object ParticleCmd : LevelCmdInterface {

    override fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val levelPlayer = PlayerManager.getLevelPlayer(sender)
            if (levelPlayer == null) {
                sender.sendMessage("你还没有加入魔法修炼")
                return true
            }
        } else {
            sender.sendMessage("只有玩家可以执行")
        }
        return true
    }
}