package com.licrafter.mc.skills.task

import com.licrafter.mc.skills.base.context.SkillController

/**
 * Created by shell on 2019/7/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class MageTask(private val controller: SkillController) : Runnable {

    override fun run() {
        if (controller.getMages().size == 0) {
            return
        }
        val iterator = controller.getMages().values.iterator()
        while (iterator.hasNext()) {
            val mage = iterator.next()
            if (mage.isActive()) {
                mage.tick()
            } else {
                iterator.remove()
            }
        }
    }
}