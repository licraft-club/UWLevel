package com.licrafter.mc.item

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.config.ItemConfig
import com.licrafter.mc.level.models.config.SkillBookConfig
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.persistence.PersistentDataType
import java.util.UUID


/**
 * Created by shell on 2019/5/26.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object ItemManager {

    private val idKey = NamespacedKey(LevelPlugin.instance(), "id")
    private val skillKey = NamespacedKey(LevelPlugin.instance(), "skill")
    private val requestLevelKey = NamespacedKey(LevelPlugin.instance(), "requestlevel")
    private val ownerKey = NamespacedKey(LevelPlugin.instance(), "owner")
    private val romanArray = arrayOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII")

    fun createItem(id: String, amount: Int = 1): ItemStack? {
        val item = LevelPlugin.itemConfig().itemMap[id] ?: return null

        val itemStack = if (item.skullValue.isEmpty()) {
            val material = Material.getMaterial(item.material) ?: return null
            ItemStack(material, amount)
        } else {
            itemFromBase64(item.skullValue)
        }

        val itemMeta = itemStack.itemMeta
        itemMeta?.let {
            it.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.display))
            val loreList = arrayListOf<String>()
            loreList.addAll(item.lores)
            it.lore = loreList.map { lore ->
                ChatColor.translateAlternateColorCodes('&', lore)
            }
            if (it is BookMeta) {
                it.author = item.author
                it.pages = item.pages
                it.title = item.title
            }
        }
        itemMeta?.addEnchant(Enchantment.PROTECTION_FALL, 1, false)

        itemMeta?.addItemFlags(ItemFlag.HIDE_PLACED_ON)
        itemMeta?.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        itemMeta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        itemMeta?.persistentDataContainer?.set(idKey, PersistentDataType.STRING, id)

        itemStack.itemMeta = itemMeta
        return itemStack
    }

    fun createSkillBook(id: String, level: Int = 1, amount: Int = 1): ItemStack? {
        val book = LevelPlugin.skillBookConfig().bookMap[id] ?: return null
        val itemStack = ItemStack(Material.ENCHANTED_BOOK, amount)
        if (level < 1) {
            return null
        }

        val itemMeta = itemStack.itemMeta
        itemMeta?.let {
            it.setDisplayName(ChatColor.translateAlternateColorCodes('&', book.display))
            val loreList = arrayListOf<String>()
            loreList.addAll(book.lores)
            it.lore = loreList.map { lore ->
                ChatColor.translateAlternateColorCodes('&', lore.replace("{reqlevel}", book.reqLevel.toString(), true))
            }
        }

        itemMeta?.persistentDataContainer?.set(idKey, PersistentDataType.STRING, id)
        itemMeta?.persistentDataContainer?.set(skillKey, PersistentDataType.STRING, "$id|$level")
        itemMeta?.persistentDataContainer?.set(requestLevelKey, PersistentDataType.INTEGER, book.reqLevel)
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    fun getId(itemStack: ItemStack): String? {
        return itemStack.itemMeta?.persistentDataContainer?.get(idKey, PersistentDataType.STRING)
    }

    fun getSkill(itemStack: ItemStack): String? {
        return itemStack.itemMeta?.persistentDataContainer?.get(skillKey, PersistentDataType.STRING)
    }

    fun setSkill(itemStack: ItemStack, skill: String) {
        val itemMeta = itemStack.itemMeta ?: return
        itemMeta.persistentDataContainer.set(skillKey, PersistentDataType.STRING, skill)
        itemStack.itemMeta = itemMeta
    }

    fun getReqLevel(itemStack: ItemStack): Int {
        return itemStack.itemMeta?.persistentDataContainer?.get(requestLevelKey, PersistentDataType.INTEGER) ?: 1
    }

    fun getOwner(itemStack: ItemStack): String? {
        return itemStack.itemMeta?.persistentDataContainer?.get(ownerKey, PersistentDataType.STRING)
    }

    fun setOwner(itemStack: ItemStack, player: Player) {
        //itemStack.itemMeta?.persistentDataContainer?.set(ownerKey, PersistentDataType.STRING, abilities.uniqueId.toString())
    }

    fun getItemConfig(itemStack: ItemStack): ItemConfig.Item? {
        val id = getId(itemStack) ?: return null
        return LevelPlugin.itemConfig().itemMap[id]
    }

    fun getRomanValue(value: Int): String? {
        return if (value < 1 || value > romanArray.size) {
            null
        } else {
            romanArray[value - 1]
        }
    }

    fun getSkillBookConfig(itemStack: ItemStack): SkillBookConfig.Book? {
        val id = getId(itemStack) ?: return null
        return LevelPlugin.skillBookConfig().bookMap[id]
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
}