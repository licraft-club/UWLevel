package com.licrafter.mc.level.models

import com.elmakers.mine.bukkit.utility.CompatibilityUtils
import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.config.LangConfig
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/9/14.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object Message : PluginInterface {

    lateinit var config: LangConfig
        private set

    override fun onEnable(plugin: LevelPlugin) {
        reload()
    }

    fun sendMessage(player: Player, message: String, vararg args: Any) {
        val showType = LevelManager.config.messageShowType
        if (showType.equals("actionbar", true)) {
            sendActionbar(player, String.format(message, *args))
        } else {
            player.sendMessage(String.format(message, *args))
        }
    }

    fun sendTitle() {

    }

    fun sendActionbar(player: Player, message: String, vararg args: Any) {
        CompatibilityUtils.sendActionBar(player, String.format(message, *args))
    }

    override fun reload() {
        YmlMaker(LevelPlugin.instance(), "languages/zh.yml").saveDefaultConfig()
        config = ParserAPI.instance().loadValues(LevelPlugin.instance(),
                "languages/" + LevelManager.config.language + ".yml", LangConfig::class.java)
    }

    override fun onDisable() {

    }
}