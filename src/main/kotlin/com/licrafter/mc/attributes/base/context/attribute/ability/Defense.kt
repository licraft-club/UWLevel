package com.licrafter.mc.attributes.base.context.attribute.ability

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.utils.LoresUtil
import java.util.*

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Defense : IAttribute {

    private var defenseRange = Pair(0, 0)

    override fun parse(lore: String) {
        LoresUtil.parseAttrRange(lore)?.let {
            defenseRange = defenseRange.copy(it.first + defenseRange.first, it.second + defenseRange.second)
        }
    }

    override fun getAttrValue(): Int {
        return if (defenseRange.first < defenseRange.second) {
            Random().nextInt(defenseRange.second - defenseRange.first) + defenseRange.first
        } else {
            defenseRange.first
        }
    }

    override fun reset() {
        defenseRange = Pair(0, 0)
    }
}