package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/6/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean(file = "particleGui.yml")
class ParticleGuiConfig {
    @ConfigSection(path = "types")
    var particleMap = HashMap<String, Particle>()

    class Particle {
        @ConfigValue(path = "name")
        var name = ""
        @ConfigValue(path = "material")
        var material = ""
    }
}