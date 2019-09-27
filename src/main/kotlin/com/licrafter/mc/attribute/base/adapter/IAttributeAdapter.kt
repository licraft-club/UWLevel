package com.licrafter.mc.attribute.base.adapter

import com.licrafter.mc.level.LevelPlugin

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
interface IAttributeAdapter {

    fun onAttch(plugin: LevelPlugin)

    /**
     * 处理拦截的入口
     */
    fun intercept()

    /**
     * 处理拦截的事件
     * @return true 消耗该事件，不往下传递。反之不消耗向下传递
     */
    fun onIntercept(): Boolean

    fun onRelease()
}