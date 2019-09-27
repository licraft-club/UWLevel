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
    @ConfigValue(path = "settings.message-showtype", defaultsTo = "actionbar")
    var messageShowType = ""
    @ConfigSection(path = "settings.prefixs")
    var prefixMap = HashMap<String, Prefix>()
    @ConfigSection(path = "settings.levels")
    var levelMap: HashMap<String, Level> = HashMap()
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
    @ConfigValue(path = "settings.wand")
    var wand = ""

    class Level : Comparable<Level> {

        @ConfigValue(path = "number")
        var number: Int = 0
        @ConfigValue(path = "maxMagic")
        var maxMagic = 0
        @ConfigValue(path = "particles")
        var particles = arrayListOf<String>()
        @ConfigSection(path = "conditions")
        var condition: Condition? = null
        @ConfigValue(path = "abilities")
        var attributes = emptyList<String>()

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
            return ("level_$number").hashCode()
        }
    }

    class Prefix : Comparable<Prefix> {
        override fun compareTo(other: Prefix): Int {
            return this.mixlevel - other.mixlevel
        }

        @ConfigValue(path = "fullname")
        var fullname = ""
        @ConfigValue(path = "mixlevel")
        var mixlevel = -1
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
        @ConfigValue(path = "items")
        var items: List<String> = arrayListOf()

        fun getItemAmountMap(): HashMap<String, Int> {
            val map = HashMap<String, Int>()
            items.forEach {
                val pair = getItemNameAmountPair(it)
                map[pair.first] = pair.second
            }
            return map
        }

        private fun getItemNameAmountPair(itemStr: String): Pair<String, Int> {
            val array = itemStr.split(",")
            var itemName = array[0]
            var itemCount = 1
            if (array.size > 1) {
                itemCount = try {
                    array[1].toInt()
                } catch (e: Exception) {
                    1
                }
            }
            return Pair(itemName, itemCount)
        }
    }

    class Wand {
        @ConfigValue(path = "displayname")
        var displayName = ""
        @ConfigValue(path = "lores")
        var lores = arrayListOf<String>()
    }

    @Nullable
    fun getLevel(number: Int): Level? {
        return levelMap.values.firstOrNull { it.number == number }
    }

    fun getLevelPrefix(level: Level?): Prefix? {
        level ?: return null
        var mixPrefix: Prefix? = null
        for (prefix in prefixMap.values) {
            if (level.number >= prefix.mixlevel && (mixPrefix == null || mixPrefix.mixlevel < prefix.mixlevel)) {
                mixPrefix = prefix
            }
        }
        return mixPrefix
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

    fun isMaxLevel(level: Level?): Boolean {
        return level != null && sortedLevels.size > 0 && sortedLevels.indexOf(level) == sortedLevels.size - 1
    }
}