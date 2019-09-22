package com.licrafter.mc.level.commands

import com.licrafter.mc.level.models.PlayerManager
import com.licrafter.mc.level.guis.AltarGui
import com.licrafter.mc.level.guis.SkillSelectGui
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object GuiCmd : LevelCmdInterface {

    override fun perform(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (args.size < 2) {
                sender.sendMessage("/levels guis [name]")
                return true
            }
            if (args[1] == "altar") {
                val altarGui = AltarGui()
                val inventory = altarGui.createAltarGui(sender)
                if (inventory == null) {
                    sender.sendMessage("创建gui失败，请查看配置文件")
                    return true
                }
                sender.openInventory(inventory)
            } else if (args[1] == "skill") {
                val levelPlayer = PlayerManager.getLevelPlayer(sender) ?: return true
                val skillGui = SkillSelectGui()
                val inventory = skillGui.create(levelPlayer)
                if (inventory == null) {
                    sender.sendMessage("创建gui失败，请查看配置文件")
                    return true
                }
                sender.openInventory(inventory)
            }
        }
        return true
    }
}