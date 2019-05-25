package com.licrafter.mc.level

import com.licrafter.lib.log.BLog
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class LevelPlugin : JavaPlugin() {

    override fun onEnable() {
        BLog.printEnableInfo(this)
    }

    override fun onDisable() {
        BLog.printDisableInfo(this)
    }
}