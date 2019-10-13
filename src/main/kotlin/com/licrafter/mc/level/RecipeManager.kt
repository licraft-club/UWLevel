package com.licrafter.mc.level

import com.licrafter.lib.log.BLog
import com.licrafter.mc.item.ItemCreateHelper
import com.licrafter.mc.level.models.config.RecipeConfig
import org.bukkit.NamespacedKey
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe


object RecipeManager {

    private lateinit var recipeMap: HashMap<String, RecipeConfig.Recipe>

    fun onEnable() {
        recipeMap = LevelPlugin.recipeConfig().recipeMap

        for (recipe in recipeMap) {
            val success = injectRecipe(recipe)
            if (success) {
                BLog.consoleMessage(LevelPlugin.instance().name, "成功注入配方 ${recipe.key}")
            } else {
                BLog.consoleMessage(LevelPlugin.instance().name, "注入配方 ${recipe.key} 发生了错误,请检查配置文件")
            }
        }
    }

    private fun injectRecipe(recipeEntry: MutableMap.MutableEntry<String, RecipeConfig.Recipe>): Boolean {
        val materialMap = recipeEntry.value.materials
        val result = ItemCreateHelper.createItem(recipeEntry.value.result) ?: return false

        val recipe = ShapedRecipe(NamespacedKey(LevelPlugin.instance(), recipeEntry.key), result)
        recipe.shape(recipeEntry.value.row_1, recipeEntry.value.row_2, recipeEntry.value.row_3)

        for (entity in materialMap) {
            val material: Material?
            if (entity.value.startsWith("minecraft:")) {
                //原生
                material = Material.getMaterial(entity.value.replace("minecraft:", "")) ?: return false
                recipe.setIngredient(entity.key.toCharArray().first(), material)
            } else {
                //自定义物品
                val item = ItemCreateHelper.createItem(entity.value) ?: return false
                recipe.setIngredient(entity.key.toCharArray().first(), RecipeChoice.ExactChoice(item))
            }
        }

        Bukkit.addRecipe(recipe)
        return true
    }
}