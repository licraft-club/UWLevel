package com.licrafter.mc.level

import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.lib.log.BLog
import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.item.ItemManager
import com.licrafter.mc.level.commands.LevelsCommand
import com.licrafter.mc.level.commands.TabComplete
import com.licrafter.mc.level.listeners.*
import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.models.Message
import com.licrafter.mc.level.models.PlayerManager
import com.licrafter.mc.level.models.config.*
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
        getCommand("levels")?.setExecutor(levelCommands)
        getCommand("levels")?.tabCompleter = TabComplete()
        server.pluginManager.registerEvents(GuiListener(), this)
        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(MythicMobListener(), this)
        server.pluginManager.registerEvents(UWLevelListener(), this)
        server.pluginManager.registerEvents(PlayerSkillListener(), this)
        initConfig()
        effectManager = EffectManager(this)
        RecipeManager.onEnable()
        LevelManager.onEnable(this)
        ItemManager.onEnable(this)
        Message.onEnable(this)
        particleManager = ParticleManager()
        AttributeManager.onEnable(this)
        initThirdParty()
    }

    fun initThirdParty() {
        SkillsManager.onEnable()
    }

    fun initConfig() {
        try {
            //初始化配置文件
            val skillBookConfig = YmlMaker(this, "items/books.yml")
            skillBookConfig.saveDefaultConfig()
            val repiceConfig = YmlMaker(this, "recipe.yml")
            repiceConfig.saveDefaultConfig()
            val skillbooks = YmlMaker(this, "items/books.yml")
            skillbooks.saveDefaultConfig()
            val altarGuiConfig = YmlMaker(this, "altarGui.yml")
            altarGuiConfig.saveDefaultConfig()
            val particleConfig = YmlMaker(this, "particleGui.yml")
            particleConfig.saveDefaultConfig()
        } catch (e: Exception) {
            BLog.warning(this, "配置文件初始化失败")
        }

        //加载配置文件

        skillBookConfig = ParserAPI.instance().loadValues(this, SkillBookConfig::class.java)
        recipeConfig = ParserAPI.instance().loadValues(this, RecipeConfig::class.java)
        altarGuiConfig = ParserAPI.instance().loadValues(this, AltarGuiConfig::class.java)
        particleGuiConfig = ParserAPI.instance().loadValues(this, ParticleGuiConfig::class.java)

    }

    fun reload() {
        initConfig()
        Message.reload()
        PlayerManager.reload()
    }

    override fun onDisable() {
        BLog.printDisableInfo(this)
        PlayerManager.onDisable()
        AttributeManager.onDisable()
        effectManager.dispose()
        SkillsManager.onDisable()
    }

    companion object {
        private lateinit var skillBookConfig: SkillBookConfig
        private lateinit var recipeConfig: RecipeConfig
        private lateinit var particleGuiConfig: ParticleGuiConfig
        private lateinit var INSTANCE: LevelPlugin
        private lateinit var effectManager: EffectManager
        private lateinit var altarGuiConfig: AltarGuiConfig
        private lateinit var particleManager: ParticleManager

        fun skillBookConfig(): SkillBookConfig {
            return skillBookConfig
        }

        fun recipeConfig(): RecipeConfig {
            return recipeConfig
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

        fun particleManager(): ParticleManager {
            return particleManager
        }

        fun instance(): LevelPlugin {
            return INSTANCE
        }
    }
}