package com.licrafter.mc.level

import com.licrafter.mc.level.particles.TrackEffect
import de.slikey.effectlib.EffectType
import org.bukkit.Particle
import org.bukkit.entity.Player


class ParticleManager {

    private val particleTypeMap = HashMap<String, Particle>()

    init {
        particleTypeMap["FLAME"] = Particle.FLAME
        particleTypeMap["HEART"] = Particle.HEART
        particleTypeMap["FIREWORKS_SPARK"] = Particle.FIREWORKS_SPARK
        particleTypeMap["COMPOSTER"] = Particle.COMPOSTER
        particleTypeMap["SMOKE_LARGE"] = Particle.SMOKE_LARGE
        particleTypeMap["CLOUD"] = Particle.CLOUD
        particleTypeMap["DRAGON_BREATH"] = Particle.DRAGON_BREATH
        particleTypeMap["DRIP_WATER"] = Particle.DRIP_WATER
        particleTypeMap["NOTE"] = Particle.NOTE
        particleTypeMap["SPELL"] = Particle.SPELL
        particleTypeMap["TOTEM"] = Particle.TOTEM
    }

    fun createTrackEffect(particle: String, player: Player): TrackEffect? {
        val particleType = particleTypeMap[particle] ?: return null
        val effect = TrackEffect(particleType, LevelPlugin.effectManager())
        effect.entity = player
        effect.asynchronous = true
        effect.type = EffectType.REPEATING
        effect.iterations = -1
        effect.particleCount = 3
        effect.period = 5
        effect.visibleRange = 20f
        return effect
    }

    fun canUseTypeMap(): HashMap<String, Particle> {
        return particleTypeMap
    }
}