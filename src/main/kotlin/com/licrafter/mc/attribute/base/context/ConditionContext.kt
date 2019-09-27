package com.licrafter.mc.attribute.base.context

import org.bukkit.inventory.ItemStack

/**
 * Created by shell on 2019/9/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class ConditionContext(val attributeData: AttributeData, val itemStack: ItemStack, val loadType: AttributeData.LoadType) {

    var shouldLoad = true

}