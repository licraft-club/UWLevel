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

/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class AttributeData private constructor(private val player: Player) {

    private val levelData = PlayerManager.getLevelPlayer(player.uniqueId)
    private val playerAbilityList = arrayListOf<IAttribute>()
    private val handWeaponAbilityList = arrayListOf<IAttribute>()

    fun reload() {
        //重置所有属性
        playerAbilityList.forEach {
            it.reset()
        }

        //加载护甲槽的装备属性
        player.equipment?.armorContents?.filter {
            it != null && it.type != Material.AIR && it.itemMeta?.hasLore() == true
        }?.forEach { armor ->
            armor?.itemMeta?.lore?.forEach {
                loadAttribute(it)
            }
        }

        //加载RPG等级符文和等级默认属性
        if (levelData != null) {
            levelData.rpgRune.filter {
                it.type != Material.AIR && it.itemMeta?.hasLore() == true
            }.forEach { rune ->
                rune.itemMeta?.lore?.forEach {
                    loadAttribute(it)
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

    fun resetPlayerAbility() {
        playerAbilityList.forEach { it.reset() }
    }

    fun resetHandWeaponAbility() {
        handWeaponAbilityList.forEach { it.reset() }
    }

    fun loadHandWeaponAttribute(lore: String) {
        for (attribute in handWeaponAbilityList) {
            if (attribute.match(lore)) {
                attribute.merge(lore)
            }
        }
    }

    private fun loadAttribute(lore: String) {
        for (attribute in playerAbilityList) {
            if (attribute.match(lore)) {
                attribute.merge(lore)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : IAttribute> getAbility(attrClass: Class<T>): Int {
        val playerAttrValue = getPlayerBaseAbility(attrClass)?.getValue() ?: 0
        val weaponAttrValue = getHandWeaponAbility(attrClass)?.getValue() ?: 0
        return playerAttrValue + weaponAttrValue
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : IAttribute> getPlayerBaseAbility(attrClass: Class<T>): T? {
        return playerAbilityList.find { it.javaClass.simpleName == attrClass.simpleName } as T?
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : IAttribute> getHandWeaponAbility(attrClass: Class<T>): T? {
        return handWeaponAbilityList.find { it.javaClass.simpleName == attrClass.simpleName } as T?
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
            data.playerAbilityList.add(attribute)
            data.handWeaponAbilityList.add(attribute)
            return this
        }

        fun build(): AttributeData {
            return data
        }
    }
}