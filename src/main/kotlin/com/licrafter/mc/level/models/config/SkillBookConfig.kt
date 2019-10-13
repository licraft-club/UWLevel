package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/7/28.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean(file = "items/books.yml")
class SkillBookConfig {

    @ConfigSection(path = "books")
    var bookMap = HashMap<String, Book>()

    class Book {
        @ConfigValue(path = "reqlevel")
        var reqLevel = 1
        @ConfigValue(path = "display")
        var display = ""
        @ConfigValue(path = "lores")
        var lores = listOf<String>()
    }
}