package com.licrafter.mc.level.commands

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.models.PlayerManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object JoinCmd : LevelCmdInterface {
    override fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            BLog.consoleMessage("&c只有玩家才可以执行该指令")
            return true
        }
        val player = sender as Player

        var levelNum = -1
        try {
            levelNum = Integer.valueOf(args[1])!!
        } catch (e: Exception) {
            //do nothing
        }

        if (levelNum == -1) {
            sender.sendMessage("请输入正确的等级数字")
            return true
        }
        val level = LevelManager.config.getLevel(levelNum)
        if (level == null) {
            sender.sendMessage("没有该等级")
            return true
        }
        val levelPlayer = PlayerManager.getLevelPlayer(sender)
        if (levelPlayer == null) {
            //还未开启魔法升级之路
            sender.sendMessage("升级成功")
            PlayerManager.createLevelPlayer(player)
        } else {
            //已经开启了魔法升级
            val oldLevel = LevelManager.config.getLevel(levelPlayer.level)
            if (oldLevel?.equals(level) == true) {
                player.sendMessage("你已经在这个等级了")
            } else {
                levelPlayer.level += 1
                player.sendMessage("升级成功")
            }
        }
        return true
    }
}