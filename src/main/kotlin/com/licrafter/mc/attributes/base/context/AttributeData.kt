package com.licrafter.mc.attributes.base.context

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.models.config.UserData

/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class AttributeData private constructor(private val levelPlayer: UserData) {

    private val abilityAttrList = arrayListOf<IAttribute>()
    private val conditionAttrList = arrayListOf<IAttribute>()

    fun load() {

        //load rpg rune attrs
        for (item in levelPlayer.rpgItem) {
            item.itemMeta?.lore?.forEach {
                loadAttribute(it)
            }
        }

        //load level default attributes
        LevelManager.config.getLevel(levelPlayer.level)?.let { level ->
            level.attributes.forEach {
                loadAttribute(it)
            }
        }
    }

    fun reset() {
        abilityAttrList.forEach { it.reset() }
    }

    private fun loadCondition(lore: String) {

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

    fun getLevelPlayer(): UserData? {
        return levelPlayer
    }

    class Builder(levelPlayer: UserData) {

        private val data = AttributeData(levelPlayer)

        fun withAbility(attribute: IAttribute): Builder {
            data.abilityAttrList.add(attribute)
            return this
        }

        fun withCondition(attribute: IAttribute): Builder {
            data.conditionAttrList.add(attribute)
            return this
        }

        fun build(): AttributeData {
            return data
        }
    }
}