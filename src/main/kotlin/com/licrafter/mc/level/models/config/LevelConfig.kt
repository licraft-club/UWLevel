package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue
import com.sun.istack.internal.Nullable
import java.util.*
import kotlin.collections.HashMap

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
    var levelMap: HashMap<String, Level> = HashMap()
    @ConfigSection(path = "settings.guis.altar-gui")
    var altarGui: GUI? = null
    @ConfigSection(path = "settings.storage")
    var storage: Storage? = null
    @Nullable
    private var sortedLevels: ArrayList<Level> = arrayListOf()
        get() {
            if (field.size == 0) {
                field.addAll(levelMap.values)
                Collections.sort(field)
            }
            return field
        }

    class Level : Comparable<Level> {

        @ConfigValue(path = "fullname")
        var fullname: String? = null
        @ConfigValue(path = "number")
        var number: Int = 0
        @ConfigSection(path = "condition")
        var condition: Condition? = null

        override fun compareTo(other: Level): Int {
            return this.number - other.number
        }

        override fun equals(other: Any?): Boolean {
            if (other == null || other !is Level) {
                return false
            }
            return number == other.number
        }

        override fun hashCode(): Int {
            return (fullname ?: "").hashCode() + number.hashCode()
        }
    }

    class GUI {
        @ConfigValue(path = "title")
        var title: String? = null
        @ConfigValue(path = "size")
        var size: Int = 0
        @ConfigValue(path = "upgrade.display")
        var upgradeDisplay: String? = null
        @ConfigValue(path = "upgrade.lores")
        var upgradeLores: List<String> = arrayListOf()
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

    class Condition {
        @ConfigValue(path = "mobkill")
        var mobkill: Int = 0
        @ConfigValue(path = "money")
        var money: Int = 0
        @ConfigValue(path = "lores")
        var lores: List<String> = arrayListOf()
    }

    @Nullable
    fun getLevel(number: Int): Level? {
        return levelMap.values.firstOrNull { it.number == number }
    }

    fun isTheLastLevel(level: Level): Boolean {
        return levelMap.size == level.number
    }

    fun getNextLevel(level: Level): Level? {
        val index = sortedLevels.indexOf(level)
        return if (index >= 0 && index + 1 < sortedLevels.size) {
            sortedLevels[index + 1]
        } else {
            null
        }
    }
}