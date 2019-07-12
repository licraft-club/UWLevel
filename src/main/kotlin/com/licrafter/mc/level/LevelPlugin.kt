package com.licrafter.mc.level

import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.commands.LevelsCommand
import com.licrafter.mc.level.commands.TabComplete
import com.licrafter.mc.level.db.DBManager
import com.licrafter.mc.level.db.Repository
import com.licrafter.mc.level.listeners.GuiListener
import com.licrafter.mc.level.listeners.MythicMobListener
import com.licrafter.mc.level.listeners.PlayerListener
import com.licrafter.mc.level.listeners.UWLevelListener
import com.licrafter.mc.level.models.config.*
import com.licrafter.mc.level.thirdparty.SkillApiManager
import com.licrafter.mc.skills.UWSkill
import com.licrafter.mc.skills.base.context.SkillController
import de.slikey.effectlib.EffectManager
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
        server.pluginManager.registerEvents(MythicMobListener(), this)
        server.pluginManager.registerEvents(UWLevelListener(), this)
        effectManager = EffectManager(this)
        playerManager = PlayerManager()
        dbManager = DBManager()
        dbManager.startDatabase()
        RecipeManager.injectRecipe()
        particleManager = ParticleManager()
        skillController = SkillController(this)
        skillController.setup()
        initThirdParty()
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
            val altarGuiConfig = YmlMaker(this, "altarGui.yml")
            altarGuiConfig.saveDefaultConfig()
            val particleConfig = YmlMaker(this, "particleGui.yml")
            particleConfig.saveDefaultConfig()
        } catch (e: Exception) {
            BLog.warning(this, "配置文件初始化失败")
        }

        //加载配置文件
        levelConfig = ParserAPI.instance().loadValues(this, LevelConfig::class.java)
        langConfig = ParserAPI.instance().loadValues(this,
                "languages/" + levelConfig.language + ".yml", LangConfig::class.java)
        itemConfig = ParserAPI.instance().loadValues(this, ItemConfig::class.java)
        altarGuiConfig = ParserAPI.instance().loadValues(this, AltarGuiConfig::class.java)
        particleGuiConfig = ParserAPI.instance().loadValues(this, ParticleGuiConfig::class.java)

    }

    private fun initThirdParty() {
        skillApiManager = SkillApiManager()
        if (skillApiManager.initialize()) {
            BLog.consoleMessage("skill api manager init success")
        } else {
            BLog.consoleMessage("skill api manager init failed")
        }
    }

    override fun onDisable() {
        BLog.printDisableInfo(this)
        effectManager.dispose()
    }

    companion object {
        private lateinit var levelConfig: LevelConfig
        private lateinit var langConfig: LangConfig
        private lateinit var itemConfig: ItemConfig
        private lateinit var particleGuiConfig: ParticleGuiConfig
        private lateinit var INSTANCE: LevelPlugin
        private lateinit var dbManager: DBManager
        private lateinit var playerManager: PlayerManager
        private lateinit var effectManager: EffectManager
        private lateinit var altarGuiConfig: AltarGuiConfig
        private lateinit var particleManager: ParticleManager
        private lateinit var skillController: SkillController

        private lateinit var skillApiManager: SkillApiManager

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

        fun altarGuiConfig(): AltarGuiConfig {
            return altarGuiConfig
        }

        fun particleGuiConfig(): ParticleGuiConfig {
            return particleGuiConfig
        }

        fun effectManager(): EffectManager {
            return effectManager
        }

        fun playerManager(): PlayerManager {
            return playerManager
        }

        fun particleManager(): ParticleManager {
            return particleManager
        }

        fun skillController(): SkillController {
            return skillController
        }

        fun skillApiManager(): SkillApiManager {
            return skillApiManager
        }

        fun getRepository(): Repository? {
            return dbManager.getRepository()
        }

        fun instance(): LevelPlugin {
            return INSTANCE
        }
    }
}