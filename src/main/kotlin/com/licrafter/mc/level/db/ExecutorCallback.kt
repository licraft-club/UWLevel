package com.licrafter.mc.level.db

import com.licrafter.mc.level.LevelPlugin
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
abstract class ExecutorCallback<in T> {


    protected abstract fun callback(value: T)

    @Synchronized
    fun runTask(value: T) {
        Bukkit.getScheduler().runTask(LevelPlugin.instance(), Runnable { callback(value) })
    }

    @Synchronized
    fun runTaskLater(plugin: Plugin, delay: Long, value: T) {
        Bukkit.getScheduler().runTaskLater(LevelPlugin.instance(),
                Runnable { callback(value) }, delay)
    }
}