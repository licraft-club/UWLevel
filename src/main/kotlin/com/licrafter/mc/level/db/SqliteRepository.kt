package com.licrafter.mc.level.db

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SqliteRepository : Repository {
    private var connection: Connection? = null

    @Synchronized override fun init() {
        try {
            val dbFile = File(LevelPlugin.instance().dataFolder.path + File.separator + "users.db")
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.absolutePath)
            initTables()
        } catch (e: Exception) {
            BLog.consoleMessage("&cInit sqlite repository error: " + e.localizedMessage)
        }
    }


    @Synchronized private fun initTables() {
        try {
            LevelPlugin.levelConfig().storage?.run {
                val connection = connection
                val create = connection?.createStatement()
                create?.executeUpdate("CREATE TABLE IF NOT EXISTS " + tablePrefix +
                        "users(`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "uuid VARCHAR(36) NOT NULL UNIQUE, " +
                        "name VARBINARY(1024), " +
                        "level INTEGER)")
            }?.apply {
                BLog.consoleMessage("&cConfig storage is null")
            }
        } catch (e: Exception) {
            BLog.consoleMessage("&cCreate Table " + LevelPlugin.levelConfig().storage?.tablePrefix + "users failed: " + e.localizedMessage)
        }
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