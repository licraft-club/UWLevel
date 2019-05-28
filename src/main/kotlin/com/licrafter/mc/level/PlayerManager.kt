package com.licrafter.mc.level

import com.licrafter.mc.level.models.LevelPlayer
import com.sun.istack.internal.Nullable
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class PlayerManager {
    private val players = ConcurrentHashMap<String, LevelPlayer>()
    private val playersUUID = ConcurrentHashMap<UUID, LevelPlayer>()

    fun addLevelPlayer(player: LevelPlayer) {
        player.getName()?.let { players.put(it, player) }
        player.getUUID()?.let { playersUUID.put(it, player) }
    }

    fun getLevelPlayer(player: Player): LevelPlayer? {
        return getLevelPlayer(player.uniqueId)
    }

    @Nullable
    fun getLevelPlayer(uuid: UUID): LevelPlayer? {
        return playersUUID[uuid]
    }
}