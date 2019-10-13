package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class HitRate : IAttribute {
    private var hitRate = 0

    override fun merge(lore: String) {
        hitRate += LoresUtil.parseAttrValue(lore)
    }

    override fun getValue(): Int {
        return hitRate
    }

    override fun getMaxValue(): Int {
        return hitRate
    }

    override fun getMinValue(): Int {
        return hitRate
    }

    override fun reset() {
        hitRate = 0
    }
}