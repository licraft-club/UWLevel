package com.licrafter.mc.skills.base.context

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillController(private val skillParams: SkillParams) {

    fun getSkillParams(): SkillParams? {
        return skillParams
    }

    fun release() {
        skillParams.release()
    }
}