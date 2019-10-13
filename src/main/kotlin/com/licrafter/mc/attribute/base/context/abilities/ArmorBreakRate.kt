package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class ArmorBreakRate : IAttribute {

    private var armorBreakRate = 0

    override fun merge(lore: String) {
        armorBreakRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getValue(): Int {
        return armorBreakRate
    }

    override fun getMinValue(): Int {
        return armorBreakRate
    }

    override fun getMaxValue(): Int {
        return armorBreakRate
    }

    override fun reset() {
        armorBreakRate = 0
    }
}