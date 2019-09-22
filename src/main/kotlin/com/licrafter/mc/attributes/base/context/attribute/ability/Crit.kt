package com.licrafter.mc.attributes.base.context.attribute.ability

import com.licrafter.mc.attributes.AttributeManager
import com.licrafter.mc.attributes.base.context.attribute.IAttribute
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

    override fun parse(lore: String) {
        LoresUtil.parseAttrRange(lore)?.let {
            critRange = Pair(critRange.first + it.first, critRange.second + it.second)
        }
    }

    override fun getAttrValue(): Int {
        return if (critRange.first < critRange.second) {
            Random().nextInt(critRange.second - critRange.first) + critRange.first
        } else {
            critRange.first
        }
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