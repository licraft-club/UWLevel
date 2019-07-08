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
    private var mSkillParams: SkillParams? = null
    private var mRunning = false

    override fun onStart(): Boolean {
        mRunning = true
        return super.onStart()
    }

    override fun getSkillParams(): SkillParams? {
        return mParentAdapter?.getSkillParams() ?: mSkillParams
    }

    override fun getSkillController(): SkillController? {
        return mParentAdapter?.getSkillController() ?: mSkillController
    }

    fun isRunning(): Boolean {
        return mRunning
    }

    fun setSkillController(controller: SkillController) {
        this.mSkillController = controller
    }

    fun setSkillParams(params: SkillParams) {
        this.mSkillParams = params
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
        mRunning = false
        mParentAdapter?.onRelease() ?: mSkillController?.release()
    }
}