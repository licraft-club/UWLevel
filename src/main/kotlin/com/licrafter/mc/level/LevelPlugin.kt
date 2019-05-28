package com.licrafter.mc.level

import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.commands.LevelsCommand
import com.licrafter.mc.level.commands.TabComplete
import com.licrafter.mc.level.db.DBManager
import com.licrafter.mc.level.db.Repository
import com.licrafter.mc.level.listeners.GuiListener
import com.licrafter.mc.level.listeners.PlayerListener
import com.licrafter.mc.level.models.RecipeManager
import com.licrafter.mc.level.models.config.ItemConfig
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
        server.pluginManager.registerEvents(PlayerListener(), this)
        playerManager = PlayerManager()
        dbManager = DBManager()
        dbManager.startDatabase()
        RecipeManager.injectRecipe()
    }

    private fun initConfig() {
        try {
            //初始化配置文件
            val levelConfig = YmlMaker(this, "config.yml")
            levelConfig.saveDefaultConfig()
            val langConfig = YmlMaker(this, "languages/zh.yml")
            langConfig.saveDefaultConfig()
            val itemConfig = YmlMaker(this, "items.yml")
            itemConfig.saveDefaultConfig()
        } catch (e: Exception) {
            BLog.warning(this, "配置文件初始化失败")
        }

        //加载配置文件
        levelConfig = ParserAPI.instance().loadValues(this, LevelConfig::class.java)
        langConfig = ParserAPI.instance().loadValues(this,
                "languages/" + levelConfig.language + ".yml", LangConfig::class.java)
        itemConfig = ParserAPI.instance().loadValues(this, ItemConfig::class.java)
    }

    override fun onDisable() {
        BLog.printDisableInfo(this)
    }

    companion object {
        private lateinit var levelConfig: LevelConfig
        private lateinit var langConfig: LangConfig
        private lateinit var itemConfig: ItemConfig
        private lateinit var INSTANCE: LevelPlugin
        private lateinit var dbManager: DBManager
        private lateinit var playerManager: PlayerManager

        fun levelConfig(): LevelConfig {
            return levelConfig
        }

        fun langConfig(): LangConfig {
            return langConfig
        }

        fun itemConfig(): ItemConfig {
            return itemConfig
        }

        fun dbManager(): DBManager {
            return dbManager
        }

        fun playerManager(): PlayerManager {
            return playerManager
        }

        fun getRepository(): Repository? {
            return dbManager.getRepository()
        }

        fun instance(): LevelPlugin {
            return INSTANCE
        }
    }
}