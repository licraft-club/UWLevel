package com.licrafter.mc.attribute.base.context.abilities

import com.licrafter.mc.attribute.AttributeManager

/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
interface IAttribute {

    fun match(lore: String): Boolean {
        val config = AttributeManager.config.attributeMap[javaClass.simpleName] ?: return false
        return lore.contains(config.display, true)
    }

    fun parse(lore: String)

    fun getAttrValue(): Int

    fun reset()
}