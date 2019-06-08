package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/5/27.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean(file = "items.yml")
class ItemConfig {
    @ConfigValue(path = "name-space-key")
    var nameSpaceKey = ""
    @ConfigSection(path = "books")
    var bookMap = HashMap<String, Book>()
    @ConfigSection(path = "items")
    var itemMap = HashMap<String, Item>()

    class Book {
        @ConfigValue(path = "material")
        var material = ""
        @ConfigValue(path = "display")
        var display = ""
        @ConfigValue(path = "amount")
        var amount = 1
        @ConfigValue(path = "author")
        var author = ""
        @ConfigValue(path = "title")
        var title = ""
        @ConfigValue(path = "pages")
        var pages = arrayListOf<String>()
        @ConfigValue(path = "lores")
        var lores = arrayListOf<String>()
    }

    class Item {
        @ConfigValue(path = "material")
        var material = ""
        @ConfigValue(path = "display")
        var display = ""
        @ConfigValue(path = "amount")
        var amount = 1
        @ConfigValue(path = "lores")
        var lores = arrayListOf<String>()
    }
}