package com.licrafter.mc.skills.adapters

import com.licrafter.mc.skills.base.adapter.SkillDefaultAdapter

/**
 * Created by shell on 2019/7/9.
 * <p>
 * Gmail: shellljx@gmail.com
 *
 * After the skill is executed, to update the status of the skill.
 */
class AfterRunAdapter : SkillDefaultAdapter() {

    override fun onStart(): Boolean {
        val skill = getSkillParams()?.skill ?: return false
        skill.starCoolDown()
        getSkillParams()?.mage?.decreaseMagicPower(skill.getNeedMagicPower())
        return super.onStart()
    }
}