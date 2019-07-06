package com.licrafter.mc.level.commands

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.db.ExecutorCallback
import com.licrafter.mc.level.models.LevelPlayer
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
        val level = LevelPlugin.levelConfig().getLevel(levelNum)
        if (level == null) {
            sender.sendMessage("没有该等级")
            return true
        }
        val levelPlayer = LevelPlugin.playerManager().getLevelPlayer(sender)
        if (levelPlayer == null) {
            //还未开启魔法升级之路
            val newLevelPlayer = LevelPlayer(player.uniqueId, player.displayName, levelNum, 0)
            LevelPlugin.getRepository()?.insertLevelPlayer(newLevelPlayer, object : ExecutorCallback<Boolean>() {
                override fun callback(value: Boolean) {
                    if (value) {
                        sender.sendMessage("加入了职业" + newLevelPlayer.getLevelPrefix()?.fullname)
                        LevelPlugin.playerManager().addLevelPlayer(newLevelPlayer)
                    }
                }
            })
        } else {
            //已经开启了魔法升级
            val oldLevel = levelPlayer.getLevel()
            if (oldLevel?.equals(level) == true) {
                player.sendMessage("你已经在这个等级了")
            } else {

            }
        }
        return true
    }
}