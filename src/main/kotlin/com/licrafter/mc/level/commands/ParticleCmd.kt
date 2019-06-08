package com.licrafter.mc.level.commands

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.ParticleManager
import org.bukkit.Particle
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
            val levelPlayer = LevelPlugin.playerManager().getLevelPlayer(sender)
            if (levelPlayer == null) {
                sender.sendMessage("你还没有加入魔法修炼")
                return true
            }
            val subOp = if (args.size > 1) args[1] else null
            if (subOp != null && subOp.equals("stop")) {
                levelPlayer.stopParticleEffect()
                return true
            }
            val effect = ParticleManager.createTrackEffect(Particle.HEART, sender)
            levelPlayer.runParticleEffect(effect)
        } else {
            sender.sendMessage("只有玩家可以执行")
        }
        return true
    }
}