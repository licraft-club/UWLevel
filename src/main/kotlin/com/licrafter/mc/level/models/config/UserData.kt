package com.licrafter.mc.level.models.config

import com.licraft.apt.config.ConfigBean
import com.licraft.apt.config.ConfigSection
import com.licraft.apt.config.ConfigValue
import org.bukkit.inventory.ItemStack

/**
 * Created by shell on 2019/7/29.
 * <p>
 * Gmail: shellljx@gmail.com
 */
@ConfigBean
class UserData {
    @ConfigValue(path = "uuid")
    var uuid: String? = null
    @ConfigValue(path = "displayname")
    var displayName: String? = null
    @ConfigValue(path = "level")
    var level = 1
    @ConfigValue(path = "mobkill")
    var mobKill = 0
    @ConfigValue(path = "magicpower")
    var magicPower = 0
    @ConfigValue(path = "magichealth")
    var magicHealth = 20.0
    @ConfigSection(path = "abilities")
    var skillMap = hashMapOf<String, SkillData>()
    @ConfigValue(path = "rpgInventory")
    var rpgRune: List<ItemStack> = emptyList()

    class SkillData {
        @ConfigValue(path = "level")
        var level = 1
        @ConfigValue(path = "xp")
        var exp = 0.0
        var lastCastTime = 0L
    }

    fun setSkillData(key: String, level: Int = 1, exp: Double = 0.0) {
        skillMap[key]?.let {
            it.level = level
            it.exp = exp
        } ?: let {
            val data = SkillData()
            data.level = level
            data.exp = exp
            skillMap[key] = data
        }
    }

    fun addSkillExp(skillKey: String, exp: Double, maxLimit: Double): Boolean {
        val skill = skillMap[skillKey] ?: return false
        //1分钟加一次
        if (System.currentTimeMillis() - skill.lastCastTime > 30 * 1000L) {
            skill.lastCastTime = System.currentTimeMillis()
            skill.exp = Math.min(skill.exp + exp, maxLimit)
            return true
        }
        return false
    }

    fun getSkillExp(skillKey: String): Double {
        return skillMap[skillKey]?.exp ?: 0.0
    }
}