package com.licrafter.mc.skills.base.context

import com.licrafter.mc.skills.ProjectileSkill
import org.bukkit.entity.Player
import java.lang.ref.WeakReference

/**
 * Created by shell on 2019/7/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Mage(player: Player) {
    private val DEFAULT_MAX_MAGIC_POWER = 100

    private val mWeakPlayer = WeakReference(player)
    private var mMagicPower = 0
    private val mActivitedSkills = mutableListOf<Skill>()

    fun initSkill(controller: SkillController) {
        val skill = ProjectileSkill(this, controller)
        if (!mActivitedSkills.contains(skill)) {
            mActivitedSkills.add(skill)
        }
    }

    fun tick() {
        increaseMagicPower(1)
        skillCoolDown()
    }

    fun getMagicPower(): Int {
        return mMagicPower
    }

    fun getActivitedSkills(): MutableList<Skill> {
        return mActivitedSkills
    }

    fun skillCoolDown() {
        if (mActivitedSkills.size == 0) {
            return
        }
        val iterator = mActivitedSkills.iterator()
        while (iterator.hasNext()) {
            val skill = iterator.next()
            skill.tick()
        }
    }

    fun increaseMagicPower(count: Int) {
        mMagicPower = Math.min(mMagicPower + count, DEFAULT_MAX_MAGIC_POWER)
    }

    fun decreaseMagicPower(count: Int) {
        mMagicPower = Math.max(mMagicPower - count, 0)
    }

    fun getPlayer(): Player? {
        return mWeakPlayer.get()
    }

    fun isActivity(): Boolean {
        return mWeakPlayer.get() != null
    }
}