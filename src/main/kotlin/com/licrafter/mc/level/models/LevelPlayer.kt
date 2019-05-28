package com.licrafter.mc.level.models

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.config.LevelConfig
import java.util.*

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class LevelPlayer(private val uuid: UUID?, private val name: String?, private var levelNumber: Int, private var mobKilled: Int) {

    private var level = LevelPlugin.levelConfig().getLevel(levelNumber)

    fun getName(): String? {
        return name
    }

    fun getUUID(): UUID? {
        return uuid
    }

    fun getLevel(): LevelConfig.Level? {
        return level
    }

    fun getMobKilled(): Int {
        return mobKilled
    }

    fun joinLevel(level: LevelConfig.Level) {
        this.level = level
    }

    fun addMobkilled(count: Int) {
        mobKilled += count
    }
}