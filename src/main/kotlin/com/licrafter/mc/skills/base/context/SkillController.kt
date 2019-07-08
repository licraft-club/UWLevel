package com.licrafter.mc.skills.base.context

import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.skills.listener.SkillPlayerListener
import com.licrafter.mc.skills.task.MageTask
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillController(private val plugin: LevelPlugin) {

    private val mActivitedMages = ConcurrentHashMap<UUID, Mage>()

    private lateinit var mMageTask: MageTask

    fun setup() {
        plugin.server.pluginManager.registerEvents(SkillPlayerListener(), plugin)

        //mage task
        mMageTask = MageTask(this)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, mMageTask, 20, 20)
    }

    fun getMage(player: Player): Mage? {
        return if (mActivitedMages.containsKey(player.uniqueId)) {
            mActivitedMages[player.uniqueId]
        } else {
            val mage = Mage(player)
            mage.initSkill(this)
            mActivitedMages[player.uniqueId] = mage
            mage
        }
    }

    fun getActivitedMages(): ConcurrentHashMap<UUID, Mage> {
        return mActivitedMages
    }


    fun release() {
    }
}