package com.licrafter.mc.attribute.base.context.conditions

import com.licrafter.mc.attribute.base.context.AttributeData

/**
 * Created by shell on 2019/9/27.
 * <p>
 * Gmail: shellljx@gmail.com
 */
interface ICondition {

    fun match(display: String, lore: String): Boolean {
        return lore.contains(display, true)
    }

    fun checkCondition(attributeData: AttributeData, loadType: AttributeData.LoadType, lore: String): Boolean
}