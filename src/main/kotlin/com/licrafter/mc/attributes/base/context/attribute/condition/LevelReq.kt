package com.licrafter.mc.attributes.base.context.attribute.condition

import com.licrafter.mc.attributes.base.context.attribute.IAttribute
import com.licrafter.mc.level.utils.LoresUtil

/**
 * Created by shell on 2019/9/20.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class LevelReq : IAttribute {

    private var levelReqValue = 0

    override fun parse(lore: String) {
        levelReqValue = LoresUtil.parseAttrValue(lore)
    }

    override fun getAttrValue(): Int {
        return levelReqValue
    }

    override fun reset() {
        levelReqValue = 0
    }
}