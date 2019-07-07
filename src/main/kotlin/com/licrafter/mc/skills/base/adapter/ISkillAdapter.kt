package com.licrafter.mc.skills.base.adapter

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
interface ISkillAdapter {

    fun start()

    fun release()

    fun onStart():Boolean

    fun onRelease()
}