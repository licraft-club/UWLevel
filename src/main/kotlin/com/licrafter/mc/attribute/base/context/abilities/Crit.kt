package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.level.utils.LoresUtil
import java.util.*

/**
 * Created by shell on 2019/9/16.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Crit : IAttribute {

    //支持范围
    private var critRange = Pair(0, 0)

    override fun merge(lore: String) {
        LoresUtil.parseAttrRange(lore)?.let {
            critRange = Pair(critRange.first + it.first, critRange.second + it.second)
        }
    }

    override fun getValue(): Int {
        return if (critRange.first < critRange.second) {
            Random().nextInt(critRange.second - critRange.first) + critRange.first
        } else {
            critRange.first
        }
    }

    override fun getMaxValue(): Int {
        return critRange.second
    }

    override fun getMinValue(): Int {
        return critRange.first
    }

    override fun match(lore: String): Boolean {
        return super.match(lore) && !matchCritRate(lore)
    }

    override fun reset() {
        critRange = Pair(0, 0)
    }

    private fun matchCritRate(lore: String): Boolean {
        val config = AttributeManager.config.attributeMap[CritRate::class.java.simpleName] ?: return false
        return lore.contains(config.display, true)
    }
}