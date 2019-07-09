package com.licrafter.mc.skills.base.adapter

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
abstract class AbsSkillAdapter<T : AbsSkillAdapter<T>> : ISkillAdapter {
    protected var mChildAdapter: T? = null
    protected var mParentAdapter: T? = null

    final override fun start() {
        val intercept = onStart()
        if (!intercept) {
            mChildAdapter?.start()
        }
        release()
    }

    final override fun release() {
        onRelease()
    }

    override fun onStart(): Boolean {
        return false
    }

    override fun onRelease() {
    }

    open fun attach(adapter: T): AbsSkillAdapter<T> {
        mChildAdapter = adapter
        @Suppress("UNCHECKED_CAST")
        mChildAdapter?.setParentAapter(this as T)
        return this
    }

    fun setParentAapter(adapter: T) {
        mParentAdapter = adapter
    }
}