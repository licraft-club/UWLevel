package com.licrafter.mc.level

import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.commands.LevelsCommand
import com.licrafter.mc.level.commands.TabComplete
import com.licrafter.mc.level.db.DBManager
import com.licrafter.mc.level.db.Repository
import com.licrafter.mc.level.listeners.GuiListener
import com.licrafter.mc.level.models.config.LangConfig
import com.licrafter.mc.level.models.config.LevelConfig
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class LevelPlugin : JavaPlugin() {

    private val levelCommands by lazy { LevelsCommand() }

    override fun onEnable() {
        INSTANCE = this
        BLog.printEnableInfo(this)
        initConfig()
        getCommand("levels")?.setExecutor(levelCommands)
        getCommand("levels")?.tabCompleter = TabComplete()
        server.pluginManager.registerEvents(GuiListener(), this)
        dbManager = DBManager()
        dbManager.startDatabase()
    }

    private fun initConfig() {
        try {
            //初始化配置文件
            val levelConfig = YmlMaker(this, "config.yml")
            levelConfig.saveDefaultConfig()
            val langConfig = YmlMaker(this, "languages/zh.yml")
            langConfig.saveDefaultConfig()
        } catch (e: Exception) {
            BLog.warning(this, "配置文件加载失败")
        }

        //加载配置文件
        levelConfig = ParserAPI.instance().loadValues(this, LevelConfig::class.java)
        langConfig = ParserAPI.instance().loadValues(this,
                "languages/" + levelConfig.language + ".yml", LangConfig::class.java)
    }

    override fun onDisable() {
        BLog.printDisableInfo(this)
    }

    companion object {
        private lateinit var levelConfig: LevelConfig
        private lateinit var langConfig: LangConfig
        private lateinit var INSTANCE: LevelPlugin
        private lateinit var dbManager: DBManager

        fun levelConfig(): LevelConfig {
            return levelConfig
        }

        fun langConfig(): LangConfig {
            return langConfig
        }

        fun dbManager(): DBManager {
            return dbManager
        }

        fun getRepository(): Repository? {
            return dbManager.getRepository()
        }

        fun instance(): LevelPlugin {
            return INSTANCE
        }
    }
}