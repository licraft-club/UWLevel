package com.licrafter.mc.level.models

import com.licraft.apt.config.ParserAPI
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.config.UserData
import com.sun.istack.internal.Nullable
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object PlayerManager : PluginInterface {

    private val players = ConcurrentHashMap<String, UserData>()
    private val playersUUID = ConcurrentHashMap<UUID, UserData>()

    override fun onEnable(plugin: LevelPlugin) {
    }

    fun loadLevelPlayer(player: Player): UserData? {
        val userFile = File(LevelPlugin.instance().dataFolder, "data/${player.uniqueId}.yml")
        if (userFile.exists()) {
            val userData = ParserAPI.instance().loadValues(LevelPlugin.instance(), "data/${player.uniqueId}.yml", UserData::class.java)
            players[player.name] = userData
            playersUUID[player.uniqueId] = userData
            return userData
        }
        return null
    }

    fun createLevelPlayer(player: Player) {
        if (playersUUID.contains(player.uniqueId)) {
            return
        }
        val userData = UserData()
        userData.level = 1
        userData.uuid = player.uniqueId.toString()
        userData.displayName = player.displayName
        userData.mobKill = 0
        playersUUID[player.uniqueId] = userData
        players[player.name] = userData
    }

    fun getLevelPlayer(player: Player?): UserData? {

        return player?.let {
            getLevelPlayer(it.uniqueId)
        } ?: run { null }
    }

    @Nullable
    fun getLevelPlayer(uuid: UUID): UserData? {
        return playersUUID[uuid]
    }

    fun getLevelPlayers(): MutableCollection<UserData> {
        return playersUUID.values
    }

    override fun reload() {
        playersUUID.keys().toList().forEach {
            val plaeyr = Bukkit.getServer().getPlayer(it) ?: return@forEach
            loadLevelPlayer(plaeyr)
        }
    }

    fun savePlayers() {
        Bukkit.getServer().onlinePlayers.forEach {
            savePlayer(it)
        }
    }

    fun savePlayer(player: Player) {
        val userData = playersUUID[player.uniqueId] ?: return
        userData.magicHealth = player.health
        ParserAPI.instance().saveValues(LevelPlugin.instance(), "data/${player.uniqueId}.yml", userData)
    }

    override fun onDisable() {
        savePlayers()
    }
}