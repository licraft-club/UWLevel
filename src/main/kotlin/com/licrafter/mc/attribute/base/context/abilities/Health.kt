package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/21.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Health : IAttribute {
    private var healthValue = 0

    override fun getValue(): Int {
        return healthValue
    }

    override fun merge(lore: String) {
        healthValue += LoresUtil.parseAttrValue(lore)
    }

    override fun getMaxValue(): Int {
        return healthValue
    }

    override fun getMinValue(): Int {
        return healthValue
    }

    override fun reset() {
        healthValue = 0
    }
}