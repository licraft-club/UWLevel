package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class CritRate : IAttribute {

    //支持范围
    private var critRate = 0

    override fun merge(lore: String) {
        critRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getValue(): Int {
        return critRate
    }

    override fun getMaxValue(): Int {
        return critRate
    }

    override fun getMinValue(): Int {
        return critRate
    }

    override fun reset() {
        critRate = 0
    }
}