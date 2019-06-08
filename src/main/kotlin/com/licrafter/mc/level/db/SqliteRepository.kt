package com.licrafter.mc.level.db

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.LevelPlayer
import com.licrafter.mc.level.models.config.LevelConfig
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SqliteRepository : Repository {

    private var connection: Connection? = null

    @Synchronized
    override fun init() {
        try {
            val dbFile = File(LevelPlugin.instance().dataFolder.path + File.separator + "users.db")
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.absolutePath)
            initTables()
        } catch (e: Exception) {
            BLog.consoleMessage("&cInit sqlite repository error: " + e.localizedMessage)
        }
    }

    @Throws(SQLException::class, ClassNotFoundException::class)
    fun openConnection(): Connection? {
        if (checkConnection()) {
            return connection
        }
        init()
        return connection
    }

    @Throws(SQLException::class)
    fun checkConnection(): Boolean {
        return connection != null && connection?.isClosed == false
    }

    @Synchronized
    private fun initTables() {
        try {
            LevelPlugin.levelConfig().storage?.run {
                val create = connection?.createStatement()
                create?.executeUpdate("CREATE TABLE IF NOT EXISTS " + tablePrefix +
                        "users(`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "uuid VARCHAR(36) NOT NULL UNIQUE, " +
                        "name VARBINARY(1024), " +
                        "level INTEGER, " +
                        "mobkill INTEGER)")
            } ?: apply {
                BLog.consoleMessage("&cConfig storage is null")
            }
        } catch (e: Exception) {
            BLog.consoleMessage("&cSqlite Create Table " + LevelPlugin.levelConfig().storage?.tablePrefix + "users failed: " + e.localizedMessage)
        }
    }

    override fun getLevelPlayer(player: Player, callback: ExecutorCallback<LevelPlayer>?) {
        object : BukkitRunnable() {
            override fun run() {
                try {
                    openConnection()?.use { connection ->
                        connection.prepareStatement("SELECT * FROM " + LevelPlugin.levelConfig().storage?.tablePrefix + "users WHERE uuid=? OR name=?;")
                                .use { statement ->
                                    statement.setString(1, player.uniqueId.toString())
                                    statement.setString(2, player.name)
                                    val resultSet = statement.executeQuery()
                                    if (resultSet.next()) {
                                        val levelPlayer = LevelPlayer(
                                                UUID.fromString(resultSet.getString("uuid")),
                                                player.displayName,
                                                resultSet.getInt("level"), 0)
                                        callback?.runTask(levelPlayer)
                                    } else {
                                        callback?.runTask(LevelPlayer.createInvalidate())
                                    }
                                    resultSet.close()
                                }
                    }
                } catch (e: SQLException) {
                    BLog.consoleMessage("&cSqlite GetLevelPlayer error: " + e.localizedMessage)
                }
            }
        }.runTaskAsynchronously(LevelPlugin.instance())
    }

    override fun insertLevelPlayer(levelPlayer: LevelPlayer, callback: ExecutorCallback<Boolean>?) {
        object : BukkitRunnable() {
            override fun run() {
                try {
                    openConnection()?.use { connection ->
                        connection.prepareStatement("INSERT INTO ${LevelPlugin.levelConfig().storage?.tablePrefix}users(uuid, name, level, mobkill) VALUES (?,?,?,?)")?.use { statement ->
                            statement.setString(1, levelPlayer.getUUID().toString())
                            statement.setString(2, levelPlayer.getName())
                            statement.setInt(3, levelPlayer.getLevel()?.number ?: 1)
                            statement.setInt(4, levelPlayer.getMobKilled())
                            val result = statement.executeUpdate() > 0
                            callback?.runTask(result)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback?.runTask(false)
                }
            }
        }.runTaskAsynchronously(LevelPlugin.instance())
    }

    override fun updateLevelPlayer(levelPlayer: LevelPlayer, nextLevel: LevelConfig.Level, callback: ExecutorCallback<Boolean>?) {
        object : BukkitRunnable() {
            override fun run() {
                try {
                    openConnection()?.use { connection ->
                        connection.prepareStatement("UPDATE ${LevelPlugin.levelConfig().storage?.tablePrefix}users SET " +
                                "uuid=?, " +
                                "name=?, " +
                                "level=?, " +
                                "mobkill=? " +
                                "WHERE uuid=?;")?.use { statement ->
                            statement.setString(1, levelPlayer.getUUID().toString())
                            statement.setString(2, levelPlayer.getName().toString())
                            statement.setInt(3, nextLevel.number)
                            statement.setInt(4, levelPlayer.getMobKilled())
                            statement.setString(5, levelPlayer.getUUID().toString())
                            val result = statement.executeUpdate()
                            callback?.runTask(result > 0)
                        }
                    }
                } catch (e: Exception) {
                    callback?.runTask(false)
                    BLog.consoleMessage("&cSqlite update levelplayer failed: " + e.localizedMessage)
                }
            }
        }.runTaskAsynchronously(LevelPlugin.instance())
    }

    override fun save() {
    }

    override fun disable() {
        save()
        try {
            connection?.close()
        } catch (e: Exception) {
        }
    }
}