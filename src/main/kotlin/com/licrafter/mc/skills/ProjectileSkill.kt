package com.licrafter.mc.skills

import com.licrafter.mc.skills.base.adapter.SkillAdapterFactory
import com.licrafter.mc.skills.base.context.Mage
import com.licrafter.mc.skills.base.context.Skill
import com.licrafter.mc.skills.base.context.SkillController

/**
 * Created by shell on 2019/7/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class ProjectileSkill(mage: Mage, controller: SkillController) : Skill(mage, controller) {
    override fun onCreate(builder: SkillAdapterFactory.AdapterChainBuilder): SkillAdapterFactory.AdapterChainBuilder {
        return builder.put(ProjectileAdapter()).put(ProjectileExplodeAdapter())
    }
}