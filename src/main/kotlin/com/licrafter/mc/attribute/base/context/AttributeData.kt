package com.licrafter.mc.attribute.base.context

import com.licrafter.mc.attribute.base.context.abilities.IAttribute
import com.licrafter.mc.attribute.events.PlayerAttributeLoadedEvent
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.models.PlayerManager
import com.licrafter.mc.level.models.config.UserData
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class AttributeData private constructor(private val player: Player) {

    private val levelData = PlayerManager.getLevelPlayer(player.uniqueId)
    private val abilityAttrList = arrayListOf<IAttribute>()

    private lateinit var loadCallback: AttributeLoadCallback

    fun reload() {
        //重置所有属性
        abilityAttrList.forEach {
            it.reset()
        }
        //加载双手武器属性
        val mainHandItem = player.inventory.itemInMainHand
        if (mainHandItem.type != Material.AIR) {
            if (loadCallback.onLoadItemAttribute(this, mainHandItem, LoadType.MAIN_HAND)) {
                mainHandItem.itemMeta?.lore?.forEach {
                    loadAttribute(it)
                }
            }
        }
        val offHandItem = player.inventory.itemInMainHand
        if (offHandItem.type != Material.AIR) {
            if (loadCallback.onLoadItemAttribute(this, offHandItem, LoadType.OFF_HAND)) {
                offHandItem.itemMeta?.lore?.forEach {
                    loadAttribute(it)
                }
            }
        }

        //加载护甲槽的装备属性
        player.equipment?.armorContents?.filter {
            it != null && it.type != Material.AIR && it.itemMeta?.hasLore() == true
        }?.forEach { armor ->
            if (loadCallback.onLoadItemAttribute(this, armor, LoadType.ARMOR)) {
                armor?.itemMeta?.lore?.forEach {
                    loadAttribute(it)
                }
            }
        }

        //加载RPG等级符文和等级默认属性
        if (levelData != null) {
            levelData.rpgRune.filter {
                it.type != Material.AIR && it.itemMeta?.hasLore() == true
            }.forEach { rune ->
                if (loadCallback.onLoadItemAttribute(this, rune, LoadType.RUNE)) {
                    rune.itemMeta?.lore?.forEach {
                        loadAttribute(it)
                    }
                }
            }

            LevelManager.config.getLevel(levelData.level)?.let { level ->
                level.attributes.forEach {
                    loadAttribute(it)
                }
            }
        }
        Bukkit.getScheduler().runTask(LevelPlugin.instance(),
                Runnable { Bukkit.getServer().pluginManager.callEvent(PlayerAttributeLoadedEvent(player, levelData, this)) })
    }

    fun reset() {
        abilityAttrList.forEach { it.reset() }
    }

    private fun loadAttribute(lore: String) {
        for (attribute in abilityAttrList) {
            if (attribute.match(lore)) {
                attribute.parse(lore)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getAbility(attrClass: Class<T>): T? {
        return abilityAttrList.find { it.javaClass.simpleName == attrClass.simpleName } as T?
    }

    fun getLevelData(): UserData? {
        return levelData
    }

    fun getPlayer(): Player {
        return player
    }

    class Builder(player: Player) {

        private val data = AttributeData(player)

        fun withAbility(attribute: IAttribute): Builder {
            data.abilityAttrList.add(attribute)
            return this
        }

        fun withLoadCallback(callback: AttributeLoadCallback): Builder {
            data.loadCallback = callback
            return this
        }

        fun build(): AttributeData {
            return data
        }
    }

    enum class LoadType {
        MAIN_HAND,
        OFF_HAND,
        ARMOR,
        RUNE
    }

    interface AttributeLoadCallback {
        fun onLoadItemAttribute(attributeData: AttributeData, itemStack: ItemStack, loadType: LoadType): Boolean
    }
}