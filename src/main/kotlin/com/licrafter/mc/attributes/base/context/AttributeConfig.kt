package com.licrafter.mc.attributes.base.context

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean(file = "attributes.yml")
class AttributeConfig {

    @ConfigSection(path = "nameHealthDisplay")
    var nameHealthDisplay: NameHealthDisplay? = null
    @ConfigSection(path = "holoDamageDisplay")
    var holoDamageDisplay: HoloDamageDisplay? = null
    @ConfigSection(path = "attributes")
    var attributeMap = hashMapOf<String, Attribute>()

    class Attribute {
        @ConfigValue(path = "display")
        var display: String = ""
    }

    class NameHealthDisplay {
        @ConfigValue(path = "maxLength")
        var maxLength = 20
        @ConfigValue(path = "duration")
        var duration = 3
        @ConfigValue(path = "lost", colorChar = '&')
        var lost = ""
        @ConfigValue(path = "remain", colorChar = '&')
        var remain = ""
        @ConfigValue(path = "format", colorChar = '&')
        var format = ""
    }

    class HoloDamageDisplay {
        @ConfigValue(path = "format", colorChar = '&')
        var format = ""
        @ConfigValue(path = "duration")
        var duration = 3
    }
}