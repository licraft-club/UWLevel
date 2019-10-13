package com.licrafter.mc.item

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

    open class Item {
        @ConfigValue(path = "material")
        var material = ""
        @ConfigValue(path = "display")
        var display = ""
        @ConfigValue(path = "lores")
        var lores = arrayListOf<String>()
        @ConfigValue(path = "effects")
        var effects = arrayListOf<String>()
        @ConfigSection(path = "persistent-data")
        var persistentData = hashMapOf<String, String>()
        @ConfigValue(path = "hide-effect")
        var hideEffect = false
    }
}