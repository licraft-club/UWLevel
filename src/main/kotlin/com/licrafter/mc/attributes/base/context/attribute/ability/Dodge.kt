package com.licrafter.mc.attributes.base.context.attribute.ability

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.utils.LoresUtil


/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Dodge : IAttribute {

    private var dodgeRate = 0

    override fun parse(lore: String) {
        dodgeRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getAttrValue(): Int {
        return dodgeRate
    }

    override fun reset() {
        dodgeRate = 0
    }
}