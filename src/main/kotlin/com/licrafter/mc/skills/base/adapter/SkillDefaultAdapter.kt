package com.licrafter.mc.skills.base.adapter

import com.licrafter.mc.skills.base.context.SkillController
import com.licrafter.mc.skills.base.context.SkillParams

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
open class SkillDefaultAdapter : AbsSkillAdapter<SkillDefaultAdapter>() {

    open fun getSkillParams(): SkillParams? {
        return mParentAdapter?.getSkillParams()
    }

    open fun getSkillController(): SkillController? {
        return mParentAdapter?.getSkillController()
    }

    override fun onRelease() {
        mParentAdapter?.onRelease()
    }
}