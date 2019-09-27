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

    @ConfigValue(path = "lang.reload-config-error", colorChar = '&')
    var loadConfigError: String = ""
    @ConfigValue(path = "lang.no-abilities-cache", colorChar = '&')
    var noPlayerCache: String = ""
    @ConfigValue(path = "mage.exp_earns", colorChar = '&')
    var mageEarnsExp = ""
}