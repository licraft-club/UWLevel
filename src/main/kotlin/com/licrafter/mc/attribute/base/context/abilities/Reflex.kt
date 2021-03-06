package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.level.utils.LoresUtil
import java.util.*

/**
 * Created by shell on 2019/9/19.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Reflex : IAttribute {
    //反射伤害的百分比
    private var reflexRange = Pair(0, 0)

    override fun merge(lore: String) {
        LoresUtil.parseAttrRange(lore)?.let {
            reflexRange = Pair(reflexRange.first + it.first, reflexRange.second + it.second)
        }
    }

    override fun getValue(): Int {
        return if (reflexRange.first < reflexRange.second) {
            Random().nextInt(reflexRange.second - reflexRange.first) + reflexRange.first
        } else {
            reflexRange.first
        }
    }

    override fun getMaxValue(): Int {
        return reflexRange.second
    }

    override fun getMinValue(): Int {
        return reflexRange.first
    }

    override fun reset() {
        reflexRange = Pair(0, 0)
    }

    override fun match(lore: String): Boolean {
        return super.match(lore) && !matchReflexRate(lore)
    }

    private fun matchReflexRate(lore: String): Boolean {
        val reflexRateConfig = AttributeManager.config.attributeMap[ReflexRate::class.java.simpleName]
        return reflexRateConfig != null && lore.contains(reflexRateConfig.display, true)
    }
}