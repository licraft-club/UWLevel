package com.licrafter.mc.level.thirdparty

import com.licrafter.lib.vector.VectorUtils
import com.licrafter.mc.level.LevelPlugin
import com.sucy.skill.SkillAPI
import com.sucy.skill.api.skills.Skill
import com.sucy.skill.api.skills.SkillShot
import com.sucy.skill.api.skills.TargetSkill
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/7/12.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillApiManager {

    private val skills = HashMap<String, Skill>()

    fun initialize(): Boolean {
        val skillApiPlugin = LevelPlugin.instance().server.pluginManager.getPlugin("SkillAPI") ?: return false
        if (skillApiPlugin.isEnabled && skillApiPlugin is SkillAPI) {
            skills.putAll(SkillAPI.getSkills())
            return true
        }
        return false
    }

    fun cast(player: Player) {
        val skill = skills["fire"]
        if (skill is SkillShot) {
            skill.cast(player, 2)
        } else if (skill is TargetSkill) {
            val target = VectorUtils.findTargetEntity(player, 10.0) ?: return
            skill.cast(player, target, 2, !SkillAPI.getSettings().canAttack(player, target))
        }
    }
}