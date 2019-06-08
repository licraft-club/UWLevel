package com.licrafter.mc.level.db

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.models.LevelPlayer
import com.licrafter.mc.level.models.config.LevelConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.sql.SQLException
import java.util.*
import javax.security.auth.login.Configuration

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class MysqlRepository : Repository {
    private var dataSource: HikariDataSource? = null

    @Synchronized
    override fun init() {
        dataSource = HikariDataSource()
        val storage = LevelPlugin.levelConfig().storage
        storage?.run {
            val url = "jdbc:mysql://$hostname/$database" + (if (database!!.contains("?")) "&" else "?") +
                    "useUnicode=true&characterEncoding=utf8&autoReConnect=$autoReconnect" + "&useSSL=$useSSl" +
                    "&verifyServerCertificate=$verifyServerCertificate"
            dataSource?.jdbcUrl = url
            dataSource?.username = username
            dataSource?.password = password
            dataSource?.maximumPoolSize = Runtime.getRuntime().availableProcessors() * 2
            dataSource?.addDataSourceProperty("cachePrepStmts", true)
            dataSource?.leakDetectionThreshold = 10000
            BLog.info(LevelPlugin.instance(), url)
            initTables()
        } ?: apply {
            BLog.consoleMessage("&cConfig storage is null")
        }
    }


    @Synchronized
    private fun initTables() {
        try {
            LevelPlugin.levelConfig().storage?.run {
                val connection = dataSource?.connection
                val create = connection?.createStatement()
                create?.executeUpdate("CREATE TABLE IF NOT EXISTS " + tablePrefix +
                        "users(`id` int AUTO_INCREMENT PRIMARY KEY, " +
                        "uuid VARCHAR(36) NOT NULL UNIQUE, " +
                        "name VARBINARY(1024), " +
                        "level INTEGER, " +
                        "mobkill INTEGER)")
            } ?: apply {
                BLog.consoleMessage("&cConfig storage is null")
            }
        } catch (e: Exception) {
            BLog.consoleMessage("&cCreate Table " + LevelPlugin.levelConfig().storage?.tablePrefix + "users failed: " + e.localizedMessage)
        }

    }

    override fun getLevelPlayer(player: Player, callback: ExecutorCallback<LevelPlayer>?) {
        object : BukkitRunnable() {
            override fun run() {
                try {
                    dataSource?.connection?.use { connection ->
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
                    BLog.consoleMessage("&cGetLevelPlayer error: " + e.localizedMessage)
                }
            }
        }.runTaskAsynchronously(LevelPlugin.instance())
    }

    override fun insertLevelPlayer(levelPlayer: LevelPlayer, callback: ExecutorCallback<Boolean>?) {
        object : BukkitRunnable() {
            override fun run() {
                try {
                    dataSource?.connection?.use { connection ->
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
                    dataSource?.connection?.use { connection ->
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
                    BLog.consoleMessage("&cMysql update levelplayer failed: " + e.localizedMessage)
                }
            }
        }.runTaskAsynchronously(LevelPlugin.instance())
    }

    override fun save() {
    }

    override fun disable() {
        save()
        dataSource?.close()
    }
}