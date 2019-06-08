package com.licrafter.mc.level.particles


import de.slikey.effectlib.Effect
import de.slikey.effectlib.EffectManager
import de.slikey.effectlib.EffectType
import de.slikey.effectlib.util.RandomUtils
import org.bukkit.Particle

/**
 * Created by shell on 2019/6/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class TrackEffect(val particle: Particle, effectManager: EffectManager) : Effect(effectManager) {

    init {
        type = EffectType.REPEATING
        period = 20
        iterations = -1
    }

    override fun onRun() {
        location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6))
        location.add(0.0, 1 + (RandomUtils.random.nextFloat()).toDouble(), 0.0)
        display(particle, location)
    }
}