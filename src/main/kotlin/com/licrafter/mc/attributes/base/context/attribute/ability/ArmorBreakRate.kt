package com.licrafter.mc.attributes.base.context.attribute.ability

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class ArmorBreakRate : IAttribute {

    private var armorBreakRate = 0

    override fun parse(lore: String) {
        armorBreakRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getAttrValue(): Int {
        return armorBreakRate
    }

    override fun reset() {
        armorBreakRate = 0
    }
}