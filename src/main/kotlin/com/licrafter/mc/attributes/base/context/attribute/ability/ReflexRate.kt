package com.licrafter.mc.attributes.base.context.attribute.ability

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/19.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class ReflexRate : IAttribute {
    private var reflexRateValue = 0

    override fun parse(lore: String) {
        reflexRateValue += LoresUtil.parseAttrValue(lore)
    }

    override fun getAttrValue(): Int {
        return reflexRateValue
    }

    override fun reset() {
        reflexRateValue  = 0
    }
}