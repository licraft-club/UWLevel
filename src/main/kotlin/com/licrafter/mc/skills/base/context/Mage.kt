package com.licrafter.mc.skills.base.context

import com.licrafter.mc.skills.ProjectileSkill
import com.licrafter.mc.skills.PushSkill
import net.minecraft.server.v1_14_R1.ChatComponentText
import net.minecraft.server.v1_14_R1.ChatMessageType
import net.minecraft.server.v1_14_R1.PacketPlayOutChat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
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
    private val mAvailableSkills = ConcurrentHashMap<String, Skill>()
    private var mActivedSkill: Skill? = null
    private var mCoolingBar: BossBar? = null
    private var mIsActive = false

    fun initSkill(controller: SkillController) {
        val skill1 = ProjectileSkill(this, controller)
        val key = ProjectileSkill::class.java.simpleName
        if (!mAvailableSkills.containsKey(key)) {
            mAvailableSkills[key] = skill1
        }
        val skill2 = PushSkill(this, controller)
        val key2 = PushSkill::class.java.simpleName
        if (!mAvailableSkills.containsKey(key2)) {
            mAvailableSkills[key2] = skill2
        }
        mActivedSkill = skill1
    }

    fun switchSkill(name: String) {
        mActivedSkill = mAvailableSkills[name] ?: return
    }

    fun tick() {
        increaseMagicPower(1)
    }

    fun getMagicPower(): Int {
        return mMagicPower
    }

    private fun increaseMagicPower(count: Int) {
        mMagicPower = Math.min(mMagicPower + count, DEFAULT_MAX_MAGIC_POWER)
        onMagicPowerChanged()
    }

    fun decreaseMagicPower(count: Int) {
        mMagicPower = Math.max(mMagicPower - count, 0)
        onMagicPowerChanged()
    }

    private fun onMagicPowerChanged() {
        if (mActivedSkill != null) {
            var remains = Math.floor(mMagicPower.toDouble() / DEFAULT_MAX_MAGIC_POWER * 40).toInt()
            val progressStr = "||||||||||||||||||||||||||||||||||||||||"
            remains = Math.min(remains, progressStr.length)
            val p2 = progressStr.substring(0, remains)
            val p3 = progressStr.substring(remains, progressStr.length)
            val ccText = ChatComponentText(ChatColor.translateAlternateColorCodes('&', "&e魔素:$mMagicPower  &b$p2&7$p3"))
            val packet = PacketPlayOutChat(ccText, ChatMessageType.GAME_INFO)
            (getPlayer() as CraftPlayer?)?.handle?.playerConnection?.sendPacket(packet)
        }
    }

    fun getAvailableSkills(): ConcurrentHashMap<String, Skill> {
        return mAvailableSkills
    }

    fun getActivedSkill(): Skill? {
        return mActivedSkill
    }

    fun showCoolingBar(skill: Skill) {
        if (skill == mActivedSkill) {
            createBarIfNeed()
            if (mActivedSkill?.getCoolingTime() == 0) {
                mCoolingBar?.removeAll()
                return
            }
            mCoolingBar?.let {
                getPlayer()?.let { player ->
                    it.addPlayer(player)
                }
                it.progress = skill.getCoolingProgress()
                val barTitle = ChatColor.translateAlternateColorCodes('&',
                        "冷却时间:{time}".replace("{time}", skill.getCoolingTime().toString(), true))
                it.setTitle(barTitle)
            }
        }
    }

    private fun createBarIfNeed() {
        if (mCoolingBar == null) {
            val barColor = BarColor.valueOf("PURPLE")
            val barStyle = BarStyle.valueOf("SOLID")
            mCoolingBar = Bukkit.getServer().createBossBar("技能冷却", barColor, barStyle, BarFlag.PLAY_BOSS_MUSIC)
        }
    }

    fun sendMessage(message: String) {
        getPlayer()?.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
    }

    fun getPlayer(): Player? {
        return mWeakPlayer.get()
    }

    fun isActive(): Boolean {
        return mWeakPlayer.get() != null && mIsActive
    }

    fun setActive(active: Boolean) {
        this.mIsActive = active
    }

    fun release() {
        setActive(false)
        mCoolingBar?.removeAll()
        mCoolingBar = null
        mAvailableSkills.values.forEach {
            it.release()
        }
    }
}