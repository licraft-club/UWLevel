package com.licrafter.mc.level

import com.licrafter.mc.level.particles.TrackEffect
import de.slikey.effectlib.EffectType
import org.bukkit.Particle
import org.bukkit.entity.Player


object ParticleManager {


    fun createTrackEffect(particle: Particle, player: Player): TrackEffect {
        val effect = TrackEffect(particle, LevelPlugin.effectManager())
        effect.entity = player
        effect.asynchronous = true
        effect.type = EffectType.REPEATING
        effect.iterations = -1
        effect.period = 5
        effect.visibleRange = 20f
        return effect
    }
}