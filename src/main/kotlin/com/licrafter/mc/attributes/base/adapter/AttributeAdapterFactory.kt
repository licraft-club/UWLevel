package com.licrafter.mc.attributes.base.adapter

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class AttributeAdapterFactory {

    class AdapterChainBuilder {
        private var root: AttributeDefaultAdapter? = null
        private var last: AttributeDefaultAdapter? = null

        fun put(adapter: AttributeDefaultAdapter): AdapterChainBuilder {
            if (root == null) {
                root = adapter
                last = adapter
            } else {
                last?.attach(adapter)
                last = adapter
            }
            return this
        }

        fun build(): AttributeDefaultAdapter? {
            return root
        }
    }
}