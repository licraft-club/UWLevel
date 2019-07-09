package com.licrafter.mc.skills.base.context

import com.licrafter.mc.skills.ProjectileSkill
import com.licrafter.mc.skills.PushSkill
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by shell on 2019/7/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class Mage(player: Player) {
    private val DEFAULT_MAX_MAGIC_POWER = 100

    private val mWeakPlayer = WeakReference(player)
    private var mMagicPower = 0
    private val mActivitedSkills = ConcurrentHashMap<String, Skill>()

    fun initSkill(controller: SkillController) {
        val skill1 = ProjectileSkill(this, controller)
        val key = ProjectileSkill::class.java.simpleName
        if (!mActivitedSkills.containsKey(key)) {
            mActivitedSkills[key] = skill1
        }
        val skill2 = PushSkill(this, controller)
        val key2 = PushSkill::class.java.simpleName
        if (!mActivitedSkills.containsKey(key2)) {
            mActivitedSkills[key2] = skill2
        }
    }

    fun tick() {
        increaseMagicPower(1)
        skillCoolDown()
    }

    fun getMagicPower(): Int {
        return mMagicPower
    }

    fun getActivitedSkills(): ConcurrentHashMap<String, Skill> {
        return mActivitedSkills
    }

    fun skillCoolDown() {
        if (mActivitedSkills.size == 0) {
            return
        }
        val iterator = mActivitedSkills.values.iterator()
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

    fun sendMessage(message: String) {
        getPlayer()?.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
    }

    fun getPlayer(): Player? {
        return mWeakPlayer.get()
    }

    fun isActivity(): Boolean {
        return mWeakPlayer.get() != null
    }
}