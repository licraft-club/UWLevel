package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil
import java.util.*


/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Damage : IAttribute {

    private var damgeRange = Pair(0, 0)

    override fun parse(lore: String) {
        LoresUtil.parseAttrRange(lore)?.let {
            damgeRange = Pair(damgeRange.first + it.first, damgeRange.second + it.second)
        }
    }

    override fun getAttrValue(): Int {
        return if (damgeRange.first < damgeRange.second) {
            Random().nextInt(damgeRange.second - damgeRange.first) + damgeRange.first
        } else {
            damgeRange.first
        }
    }

    override fun reset() {
        damgeRange = Pair(0, 0)
    }
}