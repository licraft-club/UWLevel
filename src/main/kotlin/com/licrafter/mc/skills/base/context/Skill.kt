package com.licrafter.mc.skills.base.context

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.skills.adapters.AfterRunAdapter
import com.licrafter.mc.skills.adapters.BeforeRunAdapter
import com.licrafter.mc.skills.base.adapter.SkillAdapterFactory
import com.licrafter.mc.skills.base.adapter.SkillRootAdapter
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
abstract class Skill(private val mage: Mage, private val controller: SkillController) {
    private val DEFAULT_COOL_DOWN_TIME = 5

    private var coolDownTime = 0
    private var mRootAdapter: SkillRootAdapter? = null
    private var mCoolDownRunnable: BukkitRunnable? = null
    private var mCoolDownTask: BukkitTask? = null
    private var mNeedMagicPower = 30

    init {
        create()
    }

    private fun create() {
        mRootAdapter = onCreate(
                SkillAdapterFactory.AdapterChainBuilder()
                        .put(SkillRootAdapter())
                        .put(BeforeRunAdapter())
        ).put(AfterRunAdapter()).build() as SkillRootAdapter?
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

    //run skill cool down
    fun starCoolDown() {
        coolDownTime = DEFAULT_COOL_DOWN_TIME
        try {
            startCollDownTask()
        } catch (e: Exception) {
            coolDownTime = 0
        }
    }

    private fun startCollDownTask() {
        mCoolDownRunnable = object : BukkitRunnable() {
            override fun run() {
                coolDownTime = Math.max(coolDownTime - 1, 0)
                mage.showCoolingBar(this@Skill)
                if (coolDownTime == 0) {
                    mCoolDownTask?.cancel()
                }
            }
        }
        if (mCoolDownTask == null || mCoolDownTask?.isCancelled == true) {
            mage.showCoolingBar(this)
            mCoolDownTask = mCoolDownRunnable?.runTaskTimer(LevelPlugin.instance(), 20, 20)
        }
    }

    //reture whether or not it is cooling
    fun isCooling(): Boolean {
        return coolDownTime != 0
    }

    fun getCoolingTime(): Int {
        return coolDownTime
    }

    fun getCoolingProgress(): Double {
        return coolDownTime.toDouble() / DEFAULT_COOL_DOWN_TIME
    }

    fun getNeedMagicPower(): Int {
        return mNeedMagicPower
    }

    fun release() {
        mCoolDownRunnable?.cancel()
    }

    abstract fun onCreate(builder: SkillAdapterFactory.AdapterChainBuilder): SkillAdapterFactory.AdapterChainBuilder
}