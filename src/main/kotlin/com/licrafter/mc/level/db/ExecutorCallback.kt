package com.licrafter.mc.level.db

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
abstract class ExecutorCallback<in T> : BukkitRunnable() {

    private var value: T? = null

    fun setValue(value: T?) {
        this.value = value
    }

    abstract fun callback(value: T?)

    override fun run() {
        callback(value)
    }

    @Synchronized
    fun runTask(plugin: Plugin, value: T?): BukkitTask {
        setValue(value)
        return super.runTask(plugin)
    }

    @Synchronized
    fun runTaskLater(plugin: Plugin, delay: Long, value: T?): BukkitTask {
        setValue(value)
        return super.runTaskLater(plugin, delay)
    }
}