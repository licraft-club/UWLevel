package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil


/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Dodge : IAttribute {

    private var dodgeRate = 0

    override fun merge(lore: String) {
        dodgeRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getValue(): Int {
        return dodgeRate
    }

    override fun getMaxValue(): Int {
        return dodgeRate
    }

    override fun getMinValue(): Int {
        return dodgeRate
    }

    override fun reset() {
        dodgeRate = 0
    }
}