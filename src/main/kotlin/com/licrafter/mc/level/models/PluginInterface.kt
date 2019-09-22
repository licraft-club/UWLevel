package com.licrafter.mc.level.models

import com.licrafter.mc.level.LevelPlugin

/**
 * Created by shell on 2019/9/14.
 * <p>
 * Gmail: shellljx@gmail.com
 */
interface PluginInterface {

    fun onEnable(plugin: LevelPlugin)

    fun reload()

    fun onDisable()
}