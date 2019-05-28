package com.licrafter.mc.level.models

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.config.ItemConfig
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.persistence.PersistentDataType

/**
 * Created by shell on 2019/5/26.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object ItemManager {

    fun createItem(item: ItemConfig.Item): ItemStack? {
        val material = Material.getMaterial(item.material) ?: return null
        val itemStack = ItemStack(material, item.amount)
        val itemMeta = itemStack.itemMeta
        val key = NamespacedKey(LevelPlugin.instance(), "level-tools")
        itemMeta?.let {
            it.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.display))
            it.lore = item.lores.map { lore ->
                ChatColor.translateAlternateColorCodes('&', lore)
            }
            it.persistentDataContainer.set(key, PersistentDataType.INTEGER, item.persistentData)
        }
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    fun createBook(book: ItemConfig.Book): ItemStack? {
        val material = Material.getMaterial(book.material) ?: return null
        val itemStack = ItemStack(material, book.amount)
        val itemMeta = itemStack.itemMeta
        val key = NamespacedKey(LevelPlugin.instance(), "level-tools")
        itemMeta?.let {
            it.setDisplayName(ChatColor.translateAlternateColorCodes('&', book.display))
            it.lore = book.lores.map { lore ->
                ChatColor.translateAlternateColorCodes('&', lore)
            }
            it.persistentDataContainer.set(key, PersistentDataType.INTEGER, book.persistentData)
            if (it is BookMeta) {
                it.author = book.author
                it.pages = book.pages
                it.title = book.title
            }
        }
        itemStack.itemMeta = itemMeta
        return itemStack
    }
}