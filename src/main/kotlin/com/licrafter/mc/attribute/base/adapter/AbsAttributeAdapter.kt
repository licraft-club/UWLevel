package com.licrafter.mc.attribute.base.adapter

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
abstract class AbsAttributeAdapter<T : AbsAttributeAdapter<T>> : IAttributeAdapter {
    protected var mChildAdapter: T? = null
    protected var mParentAdapter: T? = null

    final override fun intercept() {
        var intercept = onIntercept()
        if (!intercept) {
            mChildAdapter?.intercept()
        }
    }

    override fun onIntercept(): Boolean {
        return false
    }

    open fun attach(adapter: T): AbsAttributeAdapter<T> {
        mChildAdapter = adapter
        @Suppress("UNCHECKED_CAST")
        mChildAdapter?.setParentAapter(this as T)
        return this
    }

    fun setParentAapter(adapter: T) {
        mParentAdapter = adapter
    }
}