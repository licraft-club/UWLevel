package com.licrafter.mc.skills.base.adapter

import com.licrafter.mc.skills.base.context.SkillController
import com.licrafter.mc.skills.base.context.SkillParams
import java.util.*

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillRootAdapter : SkillDefaultAdapter() {

    private var mSkillController: SkillController? = null

    override fun getSkillParams(): SkillParams? {
        return mParentAdapter?.getSkillParams() ?: run {
            getSkillController()?.getSkillParams()
        }
    }

    override fun getSkillController(): SkillController? {
        return mParentAdapter?.getSkillController() ?: mSkillController
    }

    fun setSkillController(controller: SkillController) {
        this.mSkillController = controller
    }

    override fun attach(adapter: SkillDefaultAdapter): AbsSkillAdapter<SkillDefaultAdapter> {
        if (mChildAdapters == null) {
            mChildAdapters = LinkedList()
        }
        mChildAdapters?.add(adapter)
        adapter.setParentAapter(this)
        return this
    }

    override fun onRelease() {
        mParentAdapter?.onRelease() ?: mSkillController?.release()
    }
}