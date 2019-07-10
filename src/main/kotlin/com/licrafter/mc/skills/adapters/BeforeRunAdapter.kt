package com.licrafter.mc.skills.adapters

import com.licrafter.mc.skills.base.adapter.SkillDefaultAdapter

/**
 * Created by shell on 2019/7/9.
 * <p>
 * Gmail: shellljx@gmail.com
 *
 * Check if the conditions are met before the skill is executed
 */
class BeforeRunAdapter : SkillDefaultAdapter() {

    override fun onStart(): Boolean {
        val params = getSkillParams() ?: return false
        val mage = getSkillParams()?.mage ?: return false
        val skill = params.skill
        if (mage.getMagicPower() < skill.getNeedMagicPower()) {
            params.mage.sendMessage("&a魔素太低，请稍后执行!!")
            return true
        } else if (skill.isCooling()) {
            //正在冷却中
            params.mage.sendMessage("&a技能正在冷却中,等待: ${skill.getCoolingTime()} 秒")
            return true
        } else {
            return super.onStart()
        }
    }
}