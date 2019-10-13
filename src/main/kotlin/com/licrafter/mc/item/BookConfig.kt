package com.licrafter.mc.item

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/10/12.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean(file = "items/books.yml")
class BookConfig {

    @ConfigValue(path = "material")
    var material = ""
    @ConfigValue(path = "lores")
    var lores = ArrayList<String>()

    @ConfigSection(path = "skills")
    var skills = HashMap<String, Skill>()

    class Skill {
        @ConfigValue(path = "display")
        var display = ""
        @ConfigSection(path = "levels")
        var levels = hashMapOf<String, Level>()
    }

    class Level {
        @ConfigValue(path = "level")
        var level = 1
        @ConfigValue(path = "reqlevel")
        var reqlevel = 1
    }
}