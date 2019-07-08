package com.licrafter.mc.skills.base.context

import com.licrafter.mc.skills.base.adapter.SkillAdapterFactory
import com.licrafter.mc.skills.base.adapter.SkillRootAdapter

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
abstract class Skill(private val mage: Mage, private val controller: SkillController) {
    val DEFAULT_COOL_DOWN_TIME = 2

    private var coolDownTime = DEFAULT_COOL_DOWN_TIME
    private var mRootAdapter: SkillRootAdapter? = null

    init {
        create()
    }

    private fun create() {
        mRootAdapter = onCreate(
                SkillAdapterFactory.AdapterChainBuilder().put(SkillRootAdapter())
        ).build() as SkillRootAdapter?
        mRootAdapter?.setSkillController(controller)
        mRootAdapter?.setSkillParams(SkillParams(mage, this))
    }

    fun run() {
        if (mRootAdapter?.isRunning() == false) {
            mRootAdapter?.start()
        } else {
            System.out.println("too fast")
        }
    }

    //skill cool down
    fun tick() {
        coolDownTime = Math.max(coolDownTime - 1, 0)
    }

    //run skill cool down
    fun starCoolDown() {
        coolDownTime = DEFAULT_COOL_DOWN_TIME
    }

    abstract fun onCreate(builder: SkillAdapterFactory.AdapterChainBuilder): SkillAdapterFactory.AdapterChainBuilder
}