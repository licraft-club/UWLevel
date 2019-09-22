package com.licrafter.mc.level.models

import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.config.LevelConfig

object LevelManager : PluginInterface {

    lateinit var config: LevelConfig
        private set

    override fun onEnable(plugin: LevelPlugin) {
        try {
            YmlMaker(plugin, "config.yml").saveDefaultConfig()
            config = ParserAPI.instance().loadValues(plugin, LevelConfig::class.java)
            BLog.info(plugin, "&a配置文件&econfig.yml&a加载成功")
        } catch (e: Exception) {
            BLog.info(plugin, "&econfig.yml&4加载失败,请检查该配置文件是否正确")
        }
    }

    override fun reload() {
    }

    override fun onDisable() {
    }
}