package com.licrafter.mc.level.tasks

import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.models.PlayerManager
import com.licrafter.mc.level.models.config.UserData

/**
 * Created by shell on 2019/7/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class MageTask : Runnable {

    override fun run() {

        val iterator = PlayerManager.getLevelPlayers().iterator()
        while (iterator.hasNext()) {
            val player = iterator.next()
            addMagicPower(player)
        }
    }

    private fun addMagicPower(player: UserData) {
        val maxPower = LevelManager.config.getLevel(player.level)?.maxMagic ?: return
        player.magicPower = Math.min(maxPower, player.magicPower + 1)
    }
}