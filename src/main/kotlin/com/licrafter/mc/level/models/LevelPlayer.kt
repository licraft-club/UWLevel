package com.licrafter.mc.level.models

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.db.ExecutorCallback
import com.licrafter.mc.level.models.config.LevelConfig
import de.slikey.effectlib.Effect
import de.slikey.effectlib.EffectManager
import java.util.*

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class LevelPlayer(private val uuid: UUID?, private val name: String?, private var levelNumber: Int, private var mobKilled: Int) {

    private var level = LevelPlugin.levelConfig().getLevel(levelNumber)
    //running particles effect currently
    private var runningEffect: Effect? = null

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

    fun getParticleEffect(): Effect? {
        return runningEffect
    }

    fun runParticleEffect(effect: Effect) {
        if (runningEffect != null) {
            LevelPlugin.effectManager().done(runningEffect)
            runningEffect = null
        }
        runningEffect = effect
        runningEffect?.start()
    }

    fun stopParticleEffect() {
        runningEffect?.let {
            LevelPlugin.effectManager().done(it)
        }
    }

    fun joinLevel(level: LevelConfig.Level) {
        this.level = level
    }

    fun addMobkilled(count: Int) {
        mobKilled += count
    }

    fun upGrade(nextLevel: LevelConfig.Level) {
        level = nextLevel
    }

    fun invalidate(): Boolean {
        return uuid == null && name == null
    }

    companion object {
        fun createInvalidate(): LevelPlayer {
            return LevelPlayer(
                    null, null, -1, -1
            )
        }
    }
}