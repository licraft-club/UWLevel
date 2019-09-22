package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/5/27.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean(file = "items/items.yml")
class ItemConfig {
    @ConfigSection(path = "items")
    var itemMap = HashMap<String, Item>()

    class Item {
        @ConfigValue(path = "skill")
        var skill: String? = null
        @ConfigValue(path = "requestlevel")
        var requestLevel = 1
        @ConfigValue(path = "skull-value")
        var skullValue = ""
        @ConfigValue(path = "material")
        var material = ""
        @ConfigValue(path = "display")
        var display = ""
        @ConfigValue(path = "author")
        var author = ""
        @ConfigValue(path = "title")
        var title = ""
        @ConfigValue(path = "pages")
        var pages = arrayListOf<String>()
        @ConfigValue(path = "lores")
        var lores = arrayListOf<String>()
    }
}