package com.licrafter.mc.level.commands

import com.licrafter.mc.item.ItemCreateHelper
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
            sender.sendMessage("指令格式 /levels item [player] [id] [amount]")
            sender.sendMessage("指令格式 /levels book [player] [id] [level] [amount]")
            return true
        }
        val targetPlayer = Bukkit.getPlayer(args[1])
        if (targetPlayer == null) {
            sender.sendMessage("该玩家不在线或者不存在")
            return true
        }

        val item = if (args[0] == "book") {
            ItemCreateHelper.createSkillBookV2(args[2], args[3], args[4].toInt())
        } else {
            ItemCreateHelper.createItem(args[2], args[3].toInt())
        }
        if (item == null) {
            sender.sendMessage("没有该物品，请检查配置文件")
            return true
        }
        targetPlayer.inventory.addItem(item)
        return true
    }
}