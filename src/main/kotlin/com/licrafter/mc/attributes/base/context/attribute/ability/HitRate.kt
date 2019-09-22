package com.licrafter.mc.attributes.base.context.attribute.ability

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class HitRate : IAttribute {
    private var hitRate = 0

    override fun parse(lore: String) {
        hitRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getAttrValue(): Int {
        return hitRate
    }

    override fun reset() {
        hitRate = 0
    }
}