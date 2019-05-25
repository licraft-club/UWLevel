package com.licrafter.mc.level.db

import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import com.zaxxer.hikari.HikariDataSource

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class MysqlRepository : Repository {
    private var dataSource: HikariDataSource? = null

    @Synchronized override fun init() {
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
        }?:apply {
            BLog.consoleMessage("&cConfig storage is null")
        }
    }


    @Synchronized private fun initTables() {
        try {
            LevelPlugin.levelConfig().storage?.run {
                val connection = dataSource?.connection
                val create = connection?.createStatement()
                create?.executeUpdate("CREATE TABLE IF NOT EXISTS " + tablePrefix +
                        "users(`id` int AUTO_INCREMENT PRIMARY KEY, " +
                        "uuid VARCHAR(36) NOT NULL UNIQUE, " +
                        "name VARBINARY(1024), " +
                        "level INTEGER)")
            }?:apply {
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
        dataSource?.close()
    }
}