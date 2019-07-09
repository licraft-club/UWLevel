package com.licrafter.mc.skills.base.adapter

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillAdapterFactory {

    class AdapterChainBuilder {
        private var root: SkillDefaultAdapter? = null
        private var last: SkillDefaultAdapter? = null

        fun put(adapter: SkillDefaultAdapter): AdapterChainBuilder {
            if (root == null) {
                root = adapter
                last = adapter
            } else {
                last?.attach(adapter)
                last = adapter
            }
            return this
        }

        fun build(): SkillDefaultAdapter? {
            try {
                return root
            } finally {
                root = null // reset
            }
        }
    }
}