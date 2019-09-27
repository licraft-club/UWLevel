package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/21.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class FireRate : IAttribute {

    private var fireRateValue = 0

    override fun parse(lore: String) {
        fireRateValue += LoresUtil.parseAttrValue(lore)
    }

    override fun getAttrValue(): Int {
        return fireRateValue
    }

    override fun reset() {
        fireRateValue = 0
    }
}