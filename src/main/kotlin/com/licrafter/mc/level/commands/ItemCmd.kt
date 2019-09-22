package com.licrafter.mc.level.commands

import com.licrafter.mc.level.ItemManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * Created by shell on 2019/7/31.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object ItemCmd : LevelCmdInterface {
    override fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 4) {
            sender.sendMessage("指令格式 /levels give [ability] [condition] [amount]")
            sender.sendMessage("指令格式 /levels give [ability] [book|level] [amount]")
            return true
        }
        val targetPlayer = Bukkit.getPlayer(args[1])
        if (targetPlayer == null) {
            sender.sendMessage("该玩家不在线或者不存在")
            return true
        }
        val amount = try {
            args[3].toInt()
        } catch (e: Exception) {
            1
        }
        val array = args[2].split("|")
        val item = if (array.size > 1) {
            val book = array[0]
            val level = array[1].toInt()
            ItemManager.createSkillBook(book, level, amount)
        } else {
            ItemManager.createItem(array[0], amount)
        }
        if (item == null) {
            sender.sendMessage("没有该物品，请检查配置文件")
            return true
        }
        targetPlayer.inventory.addItem(item)
        return true
    }
}