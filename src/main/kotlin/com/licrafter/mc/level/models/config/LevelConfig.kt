package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue
import com.sun.istack.internal.Nullable
import java.util.*

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean
class LevelConfig {
    @ConfigValue(path = "settings.server")
    var server: String? = null
    @ConfigValue(path = "settings.language")
    var language: String? = null
    @ConfigSection(path = "settings.levels")
    var levelMap: HashMap<String, Level>? = null
    @ConfigSection(path = "settings.guis.altar-gui")
    var altarGui: GUI? = null
    @ConfigSection(path = "settings.storage")
    var storage: Storage? = null
    @Nullable
    private var sortedLevels: ArrayList<Level>? = null

    class Level : Comparable<Level> {

        @ConfigValue(path = "fullname")
        var fullname: String? = null
        @ConfigValue(path = "number")
        var number: Int = 0

        override fun compareTo(other: Level): Int {
            return this.number - other.number
        }
    }

    class GUI {
        @ConfigValue(path = "title")
        var title: String? = null
        @ConfigValue(path = "size")
        var size: Int? = null
        @ConfigValue(path = "upgrade.display")
        var upgradeDisplay: String? = null
        @ConfigValue(path = "upgrade.lores")
        var upgradeLores: List<String>? = null
    }

    class Storage {
        @ConfigValue(path = "type")
        var type: String? = null
        @ConfigValue(path = "mysql.username")
        var username: String? = null
        @ConfigValue(path = "mysql.password")
        var password: String? = null
        @ConfigValue(path = "mysql.hostname")
        var hostname: String? = null
        @ConfigValue(path = "mysql.database")
        var database: String? = null
        @ConfigValue(path = "mysql.table-prefix")
        var tablePrefix: String? = null
        @ConfigValue(path = "mysql.verify-server-certificate")
        var verifyServerCertificate: Boolean = false
        @ConfigValue(path = "mysql.use-ssl")
        var useSSl: Boolean = false
        @ConfigValue(path = "auto-reconnect")
        var autoReconnect: Boolean = false
    }

    fun getSrotedLevels(): List<Level> {
        return sortedLevels?.let {
            it
        } ?: run {
            sortedLevels = ArrayList(levelMap?.values)
            Collections.sort(sortedLevels)
            sortedLevels!!
        }
    }
}