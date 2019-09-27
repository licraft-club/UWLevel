package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/21.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class BlindRate : IAttribute {
    private var blindRate = 0

    override fun parse(lore: String) {
        blindRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getAttrValue(): Int {
        return blindRate
    }

    override fun reset() {
        blindRate = 0
    }
}