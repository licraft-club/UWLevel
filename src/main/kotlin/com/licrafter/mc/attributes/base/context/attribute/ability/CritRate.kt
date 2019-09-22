package com.licrafter.mc.attributes.base.context.attribute.ability

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class CritRate : IAttribute {

    //支持范围
    private var critRate = 0

    override fun parse(lore: String) {
        critRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getAttrValue(): Int {
        return critRate
    }

    override fun reset() {
        critRate = 0
    }
}