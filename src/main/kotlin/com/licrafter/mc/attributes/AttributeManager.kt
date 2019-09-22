package com.licrafter.mc.attributes

import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.lib.log.BLog
import com.licrafter.mc.attributes.adapters.*
import com.licrafter.mc.attributes.base.adapter.AttributeAdapterFactory
import com.licrafter.mc.attributes.base.context.AttributeConfig
import com.licrafter.mc.attributes.base.context.AttributeData
import com.licrafter.mc.attributes.base.context.attribute.ability.*
import com.licrafter.mc.attributes.events.AttributeLoadEvent
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.events.LevelPlayerLoadedEvent
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

/**
 * Created by shell on 2019/9/14.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object AttributeManager : Listener {

    lateinit var config: AttributeConfig
        private set
    private lateinit var mAttributeRootAdapter: AttributeRootAdapter
    val mPlayerCache = hashMapOf<UUID, AttributeData>()

    fun onEnable(plugin: LevelPlugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
        initConfig(plugin)
        mAttributeRootAdapter = AttributeAdapterFactory.AdapterChainBuilder()
                .put(AttributeRootAdapter())
                .put(DamageAdapter())   //计算对战双方的闪避可能，伤害值，防御值
                .put(EffectAdapter())   //计算药水、闪电类属性的发生率
                .put(HoloAdapter())     //展示一些血条和伤害值
                .put(EventAdapter())    //处理一些事件监听
                .build() as AttributeRootAdapter
        mAttributeRootAdapter.onAttch(plugin)

        BLog.info(plugin, "&eRPG玩家属性模块初始化完毕!!")
    }

    private fun initConfig(plugin: LevelPlugin) {
        YmlMaker(plugin, "attributes.yml").saveDefaultConfig()
        config = ParserAPI.instance().loadValues(plugin, AttributeConfig::class.java)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamageByEntity(event: EntityDamageByEntityEvent) {
        //过滤掉 mob vs mob
        if (event.entityType == EntityType.PLAYER || event.damager.type == EntityType.PLAYER) {
            mAttributeRootAdapter.onEvent(event)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerLoad(event: LevelPlayerLoadedEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(LevelPlugin.instance(), Runnable {
            val rpgInventoryItems = event.levelPlayer.rpgItem
            if (rpgInventoryItems.isNotEmpty()) {
                val attributeData = AttributeData.Builder(event.levelPlayer)
                        .withAbility(Crit())
                        .withAbility(CritRate())
                        .withAbility(Damage())
                        .withAbility(HitRate())
                        .withAbility(Defense())
                        .withAbility(Dodge())
                        .withAbility(Health())
                        .withAbility(BlindRate())
                        .withAbility(FireRate())
                        .build()
                attributeData.load()
                mPlayerCache[event.player.uniqueId] = attributeData

                Bukkit.getScheduler().runTask(LevelPlugin.instance(),
                        Runnable { Bukkit.getServer().pluginManager.callEvent(AttributeLoadEvent(event.player, event.levelPlayer, attributeData)) })
            }
        })
    }

    fun onDisable() {
        mAttributeRootAdapter.onRelease()
    }
}