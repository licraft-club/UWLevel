package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/6/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean(file = "altarGui.yml")
class AltarGuiConfig {
    @ConfigValue(path = "title")
    var title: String = ""
    @ConfigValue(path = "size")
    var size: Int = 0
    @ConfigValue(path = "upgrade.display")
    var upgradeDisplay: String? = null
    @ConfigValue(path = "upgrade.lores")
    var upgradeLores: List<String> = arrayListOf()
}