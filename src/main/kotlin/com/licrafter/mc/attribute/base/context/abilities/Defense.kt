package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.level.utils.LoresUtil
import java.util.*

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Defense : IAttribute {

    private var defenseRange = Pair(0, 0)

    override fun merge(lore: String) {
        LoresUtil.parseAttrRange(lore)?.let {
            defenseRange = defenseRange.copy(it.first + defenseRange.first, it.second + defenseRange.second)
        }
    }

    override fun getValue(): Int {
        return if (defenseRange.first < defenseRange.second) {
            Random().nextInt(defenseRange.second - defenseRange.first) + defenseRange.first
        } else {
            defenseRange.first
        }
    }

    override fun getMaxValue(): Int {
        return defenseRange.second
    }

    override fun getMinValue(): Int {
        return defenseRange.first
    }

    override fun reset() {
        defenseRange = Pair(0, 0)
    }
}