package com.licrafter.mc.attribute.base.context.conditions

import com.licrafter.mc.attribute.base.context.AttributeData
import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/27.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class LevelCondition : ICondition {

    override fun checkCondition(attributeData: AttributeData, loadType: AttributeData.LoadType, lore: String): Boolean {
        val ownerLevel = attributeData.getLevelData()?.level ?: return false
        val level = LoresUtil.parseAttrValue(lore)
        return ownerLevel >= level
    }
}