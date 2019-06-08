package com.licrafter.mc.level

import com.licrafter.mc.level.ItemManager
import com.licrafter.mc.level.LevelPlugin
import org.bukkit.NamespacedKey
import org.bukkit.Bukkit
import org.bukkit.inventory.ShapedRecipe


object RecipeManager {

    fun injectRecipe() {
        val item = ItemManager.createItem("item1", LevelPlugin.itemConfig().itemMap["item1"]!!) ?: return
        val result = ItemManager.createBook("bk1", LevelPlugin.itemConfig().bookMap["bk1"]!!) ?: return

        val key = NamespacedKey(LevelPlugin.instance(), "emerald_sword")
        val recipe = ShapedRecipe(key, result)
        recipe.shape(" a ", " a ", " a ")
        recipe.setIngredient('a', item.type)
        Bukkit.addRecipe(recipe)
    }
}