package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/21.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class FireRate : IAttribute {

    private var fireRateValue = 0

    override fun merge(lore: String) {
        fireRateValue += LoresUtil.parseAttrValue(lore)
    }

    override fun getValue(): Int {
        return fireRateValue
    }

    override fun getMaxValue(): Int {
        return fireRateValue
    }

    override fun getMinValue(): Int {
        return fireRateValue
    }

    override fun reset() {
        fireRateValue = 0
    }
}