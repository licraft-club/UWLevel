package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue

/**
 * Created by shell on 2019/7/31.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean(file = "recipe.yml")
class RecipeConfig {

    @ConfigSection(path = "")
    var recipeMap = hashMapOf<String, Recipe>()

    class Recipe {
        @ConfigValue(path = "material")
        var materials = hashMapOf<String, String>()
        @ConfigValue(path = "row_1")
        var row_1 = ""
        @ConfigValue(path = "row_2")
        var row_2 = ""
        @ConfigValue(path = "row_3")
        var row_3 = ""
        @ConfigValue(path = "result")
        var result = ""
    }
}