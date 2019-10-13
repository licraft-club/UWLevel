package com.licrafter.mc.item

import com.licrafter.mc.item.events.LoreVariablesParseEvent
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.config.SkillBookConfig
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.EnchantmentWrapper
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.util.UUID


/**
 * Created by shell on 2019/5/26.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object ItemCreateHelper {

    private val idKey = NamespacedKey(LevelPlugin.instance(), "id")
    private val levelKey = NamespacedKey(LevelPlugin.instance(), "level")
    private val requestLevelKey = NamespacedKey(LevelPlugin.instance(), "requestlevel")
    private val ownerKey = NamespacedKey(LevelPlugin.instance(), "owner")
    private val romanArray = arrayOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII")

    fun createItem(id: String, amount: Int = 1, variables: HashMap<String, String>? = null): ItemStack? {
        val itemConfig = ItemManager.itemConfig ?: return null
        val item = itemConfig.itemMap[id] ?: return null
        return createItem(id, item, amount, variables)
    }

    fun createSkillBookV2(id: String, level: String, amount: Int = 1): ItemStack? {
        val bookConfig = ItemManager.bookConfig ?: return null
        val skillConfig = bookConfig.skills[id] ?: return null
        val material = Material.ENCHANTED_BOOK
        val itemStack = ItemStack(material, amount)
        val itemMeta = itemStack.itemMeta ?: return null
        val skillLevel = skillConfig.levels[level] ?: return null

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "技能秘籍"))

        val map = hashMapOf<String, String>()
        map["display"] = "技能名字"
        map["level"] = skillLevel.level.toString()
        map["reqlevel"] = skillLevel.reqlevel.toString()
        parseLores(itemMeta, bookConfig.lores, map)

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        itemMeta.persistentDataContainer.set(idKey, PersistentDataType.STRING, id)
        itemMeta.persistentDataContainer.set(levelKey, PersistentDataType.STRING, skillLevel.level.toString())


        itemStack.itemMeta = itemMeta
        return itemStack
    }

    private fun createItem(id: String, item: ItemConfig.Item, amount: Int, variables: HashMap<String, String>?): ItemStack? {
        val itemStack = if (item.material.length < 50) {
            val material = Material.getMaterial(item.material) ?: return null
            ItemStack(material, amount)
        } else {
            itemFromBase64(item.material)
        }

        val itemMeta = itemStack.itemMeta
        itemMeta?.let {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.display))
            parseLores(it, item.lores, variables)
        }
        item.effects.forEach {
            val enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(it)) ?: return@forEach
            itemMeta?.addEnchant(enchantment, 1, false)
        }

        //所有物品都有一个id
        itemMeta?.persistentDataContainer?.set(idKey, PersistentDataType.STRING, id)

        item.persistentData.forEach {
            val spacedKey = NamespacedKey(LevelPlugin.instance(), it.key)
            itemMeta?.persistentDataContainer?.set(spacedKey, PersistentDataType.STRING, it.value)
        }

        itemMeta?.addItemFlags(ItemFlag.HIDE_PLACED_ON)
        itemMeta?.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        if (item.hideEffect) {
            itemMeta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        itemStack.itemMeta = itemMeta
        return itemStack
    }

    fun updateLores(itemStack: ItemStack, variables: HashMap<String, String>? = null) {
        val itemConfig = ItemManager.itemConfig ?: return
        val id = getId(itemStack) ?: return
        val itemMeta = itemStack.itemMeta ?: return
        val config = itemConfig.itemMap[id] ?: return
        parseLores(itemMeta, config.lores, variables)
        itemStack.itemMeta = itemMeta
    }

    fun parseLores(itemMeta: ItemMeta, lores: List<String>, variables: HashMap<String, String>? = null) {
        val loreList = arrayListOf<String>()
        loreList.addAll(lores)
        itemMeta.lore = loreList.map { it ->
            var originLore = it
            if (variables != null && variables.isNotEmpty()) {
                val variableStart = originLore.indexOf("<%")
                val variableEnd = originLore.indexOf("%>")
                if (variableStart > 0 && variableEnd > 0 && variableStart + 2 < variableEnd) {
                    val key = originLore.substring(variableStart + 2, variableEnd)
                    val value = variables[key]
                    if (value != null) {
                        val event = LoreVariablesParseEvent(key, value)
                        Bukkit.getServer().pluginManager.callEvent(event)
                        originLore = originLore.replace("<%$key%>", value)
                    }
                }
            }
            ChatColor.translateAlternateColorCodes('&', originLore)
        }
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
        itemMeta?.persistentDataContainer?.set(requestLevelKey, PersistentDataType.INTEGER, book.reqLevel)
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    fun getId(itemStack: ItemStack): String? {
        return itemStack.itemMeta?.persistentDataContainer?.get(idKey, PersistentDataType.STRING)
    }

    fun getSkill(itemStack: ItemStack): String? {
        return itemStack.itemMeta?.persistentDataContainer?.get(idKey, PersistentDataType.STRING)
    }

    fun setSkill(itemStack: ItemStack, skill: String) {
        val itemMeta = itemStack.itemMeta ?: return
        itemMeta.persistentDataContainer.set(idKey, PersistentDataType.STRING, skill)
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
        val itemConfig = ItemManager.itemConfig ?: return null
        val id = getId(itemStack) ?: return null
        return itemConfig.itemMap[id]
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