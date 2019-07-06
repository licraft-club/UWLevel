package com.licrafter.mc.level

import com.licrafter.mc.level.models.config.ItemConfig
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.Bukkit
import java.util.UUID


/**
 * Created by shell on 2019/5/26.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object ItemManager {

    fun createItem(key: String, item: ItemConfig.Item): ItemStack? {
        val itemStack = itemFromBase64(item.value)
        val itemMeta = itemStack.itemMeta
        itemMeta?.let {
            it.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.display))
            val loreList = arrayListOf<String>()
            loreList.addAll(item.lores)
            loreList.add(hideLore(key))
            it.lore = loreList.map { lore ->
                ChatColor.translateAlternateColorCodes('&', lore)
            }
        }
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    fun createBook(key: String, book: ItemConfig.Book): ItemStack? {
        val material = Material.getMaterial(book.material) ?: return null
        val itemStack = ItemStack(material, book.amount)
        val itemMeta = itemStack.itemMeta
        itemMeta?.let {
            it.setDisplayName(ChatColor.translateAlternateColorCodes('&', book.display))
            val loreList = arrayListOf<String>()
            loreList.addAll(book.lores)
            loreList.add(hideLore(key))
            it.lore = loreList.map { lore ->
                ChatColor.translateAlternateColorCodes('&', lore)
            }
            if (it is BookMeta) {
                it.author = book.author
                it.pages = book.pages
                it.title = book.title
            }
        }
        itemStack.itemMeta = itemMeta
        return itemStack
    }


    private fun itemFromBase64(base64: String): ItemStack {
        val item = getPlayerSkullItem()
        return itemWithBase64(item, base64)
    }

    private fun getPlayerSkullItem(): ItemStack {
        return if (newerApi()) {
            ItemStack(Material.valueOf("PLAYER_HEAD"))
        } else {
            ItemStack(Material.valueOf("SKULL_ITEM"), 1, 3.toByte().toShort())
        }
    }

    private fun itemWithBase64(item: ItemStack, base64: String): ItemStack {
        val hashAsId = UUID(base64.hashCode().toLong(), base64.hashCode().toLong())
        return Bukkit.getUnsafe().modifyItemStack(item,
                "{SkullOwner:{Id:\"$hashAsId\",Properties:{textures:[{Value:\"$base64\"}]}}}"
        )
    }

    private fun newerApi(): Boolean {
        try {
            Material.valueOf("PLAYER_HEAD")
            return true
        } catch (e: IllegalArgumentException) { // If PLAYER_HEAD doesn't exist
            return false
        }

    }

    private fun hideLore(lore: String): String {
        val stringBuilder = StringBuilder()
        lore.toCharArray().forEach {
            stringBuilder.append(ChatColor.COLOR_CHAR).append(it)
        }
        return stringBuilder.toString()
    }
}