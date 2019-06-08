package com.licrafter.mc.level.db

import com.licrafter.mc.level.models.LevelPlayer
import com.licrafter.mc.level.models.config.LevelConfig
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
interface Repository {
    fun init()

    /**
     * get levelplayer from db
     */
    fun getLevelPlayer(player: Player, callback: ExecutorCallback<LevelPlayer>?)

    /**
     * save levelplayer to db
     */
    fun insertLevelPlayer(levelPlayer: LevelPlayer, callback: ExecutorCallback<Boolean>?)

    /**
     * update levelplayer
     */
    fun updateLevelPlayer(levelPlayer: LevelPlayer, nextLevel: LevelConfig.Level, callback: ExecutorCallback<Boolean>?)

    fun save()

    fun disable()
}