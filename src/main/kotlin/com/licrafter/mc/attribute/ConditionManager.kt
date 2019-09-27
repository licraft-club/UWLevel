package com.licrafter.mc.attribute

import com.licrafter.lib.log.BLog
import com.licrafter.mc.attribute.base.context.AttributeData
import com.licrafter.mc.attribute.base.context.conditions.*
import com.licrafter.mc.level.LevelPlugin
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

object ConditionManager : Listener {

    private const val LEVEL_CONDITION = "Level"
    private const val MAINHAND_CONDITION = "MainHand"
    private const val OFFHAND_CONDITION = "OffHand"
    private const val ARMOR_CONDITION = "Armor"
    private const val RUNE_CONDITION = "Rune"
    private val conditionMap = hashMapOf<String, ICondition>()

    fun onEnable(plugin: LevelPlugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)

        conditionMap[LEVEL_CONDITION] = LevelCondition()
        conditionMap[MAINHAND_CONDITION] = MainHandCondition()
        conditionMap[OFFHAND_CONDITION] = OffHandCondition()
        conditionMap[ARMOR_CONDITION] = ArmorCondition()
        conditionMap[RUNE_CONDITION] = RuneCondition()
    }

    fun registerCondition(conditionKey: String, condition: ICondition) {
        if (!conditionMap.containsKey(conditionKey)) {
            conditionMap[conditionKey] = condition
        } else {
            BLog.info(LevelPlugin.instance(), "已经注册了条件 $conditionKey")
        }
    }

    /**
     * @return true 允许加载 false 不允许加载
     */
    fun checkConditions(attributeData: AttributeData, itemStack: ItemStack, loadType: AttributeData.LoadType): Boolean {
        val lores = itemStack.itemMeta?.lore ?: return false
        if (lores.isEmpty()) {
            return false
        }
        var shouldLoad = true
        lores.forEach lore@{ lore ->
            conditionMap.keys.forEach condition@{ key ->
                val config = AttributeManager.config.attributeMap[key] ?: return@condition
                val condition = conditionMap[key] ?: return@condition
                if (condition.match(config.display, lore)) {
                    shouldLoad = shouldLoad && condition.checkCondition(attributeData, loadType, lore)
                    if (!shouldLoad) {
                        return false
                    }
                }
            }
        }
        return shouldLoad
    }

}