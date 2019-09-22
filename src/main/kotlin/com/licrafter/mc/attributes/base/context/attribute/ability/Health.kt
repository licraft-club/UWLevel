package com.licrafter.mc.attributes.base.context.attribute.ability

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/21.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Health : IAttribute {
    private var healthValue = 0

    override fun getAttrValue(): Int {
        return healthValue
    }

    override fun parse(lore: String) {
        healthValue += LoresUtil.parseAttrValue(lore)
    }

    override fun reset() {
        healthValue = 0
    }
}