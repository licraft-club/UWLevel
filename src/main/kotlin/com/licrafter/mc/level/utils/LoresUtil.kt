package com.licrafter.mc.level.utils

import com.elmakers.mine.bukkit.api.spell.SpellTemplate
import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.LevelPlugin
import org.bukkit.ChatColor

/**
 * Created by shell on 2019/9/9.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object LoresUtil {

    fun getExtraLores(skill: SpellTemplate): List<String> {
        return when (skill.key) {
            "AquaBeam", "arrow" -> {
                format1(skill)
            }
            "laser" -> {
                format2(skill)
            }
            else -> {
                format1(skill)
            }
        }
    }

    private fun format1(skill: SpellTemplate): List<String> {
        return listOf("&e伤害: &b${skill.spellParameters.getInt("damage")}", "&e距离: &b${skill.spellParameters.getInt("range")}")
    }

    private fun format2(skill: SpellTemplate): List<String> {
        return listOf("&e伤害: &b${skill.spellParameters.getInt("entity_damage")}", "&e距离: &b${skill.spellParameters.getInt("range")}")
    }

    fun parseAttrRange(lore: String): Pair<Int, Int>? {
        var pair: Pair<Int, Int>? = null
        try {
            val array = (ChatColor.stripColor(lore) ?: "")
                    .replace(Regex("[^0-9]+"), ",")
                    .split(",").filter { it.isNotEmpty() }.map { it.toInt() }
            if (array.size == 1) {
                pair = Pair(array[0], array[0])
            } else if (array.size > 1) {
                pair = Pair(array[0], array[1])
            }
        } catch (e: Exception) {
            BLog.info(LevelPlugin.instance(), "$lore parse error")
        }
        return pair
    }

    fun parseAttrValue(lore: String): Int {
        try {
            val array = (ChatColor.stripColor(lore) ?: "")
                    .replace(Regex("[^0-9]+"), ",")
                    .split(",").filter { it.isNotEmpty() }
                    .map { it.toInt() }
            if (array.isNotEmpty()) {
                return array[0]
            }
        } catch (e: Exception) {
            BLog.info(LevelPlugin.instance(), "$lore parse error")
        }
        return 0
    }
}