package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/19.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class ReflexRate : IAttribute {
    private var reflexRateValue = 0

    override fun merge(lore: String) {
        reflexRateValue += LoresUtil.parseAttrValue(lore)
    }

    override fun getValue(): Int {
        return reflexRateValue
    }

    override fun getMaxValue(): Int {
        return reflexRateValue
    }

    override fun getMinValue(): Int {
        return reflexRateValue
    }

    override fun reset() {
        reflexRateValue = 0
    }
}