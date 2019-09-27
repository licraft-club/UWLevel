package com.licrafter.mc.attribute.base.context.conditions

import com.licrafter.mc.attribute.base.context.AttributeData

/**
 * Created by shell on 2019/9/27.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class RuneCondition : ICondition {
    override fun checkCondition(attributeData: AttributeData, loadType: AttributeData.LoadType, lore: String): Boolean {
        return loadType == AttributeData.LoadType.RUNE
    }
}