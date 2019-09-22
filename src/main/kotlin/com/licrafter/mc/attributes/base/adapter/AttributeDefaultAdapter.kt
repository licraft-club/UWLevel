package com.licrafter.mc.attributes.base.adapter

import com.licrafter.mc.attributes.base.context.AttributeConfig
import com.licrafter.mc.attributes.base.context.AttributeContext
import com.licrafter.mc.level.LevelPlugin

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
open class AttributeDefaultAdapter : AbsAttributeAdapter<AttributeDefaultAdapter>() {

    override fun onAttch(plugin: LevelPlugin) {
        mChildAdapter?.onAttch(plugin)
    }

    protected open fun getConfig(): AttributeConfig? {
        return mParentAdapter?.getConfig()
    }

    protected open fun getContext(): AttributeContext? {
        return mParentAdapter?.getContext()
    }

    override fun onRelease() {
        mChildAdapter?.onRelease()
    }
}