package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean
class LangConfig {

    @ConfigValue(path = "lang.load-config-error")
    var loadConfigError: String = ""
    @ConfigValue(path = "lang.no-player-cache")
    var noPlayerCache: String = ""
}