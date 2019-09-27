package com.licrafter.mc.attribute

import com.licraft.apt.config.ParserAPI
import com.licraft.apt.utils.YmlMaker
import com.licrafter.lib.log.BLog
import com.licrafter.mc.attribute.adapters.*
import com.licrafter.mc.attribute.base.adapter.AttributeAdapterFactory
import com.licrafter.mc.attribute.base.context.AttributeConfig
import com.licrafter.mc.attribute.base.context.AttributeData
import com.licrafter.mc.attribute.base.context.abilities.*
import com.licrafter.mc.level.LevelPlugin
import com.licrafter.mc.level.events.LevelPlayerLoadedEvent
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * Created by shell on 2019/9/14.
 * <p>
 * Gmail: shellljx@gmail.com
 */
object AttributeManager : Listener, AttributeData.AttributeLoadCallback {

    lateinit var config: AttributeConfig
        private set
    private lateinit var mAttributeRootAdapter: AttributeRootAdapter
    val mPlayerCache = hashMapOf<UUID, AttributeData>()

    fun onEnable(plugin: LevelPlugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
        initConfig(plugin)
        mAttributeRootAdapter = AttributeAdapterFactory.AttributeChainBuilder()
                .put(AttributeRootAdapter())
                .put(DamageAdapter())   //计算对战双方的闪避可能，伤害值，防御值
                .put(EffectAdapter())   //计算药水、闪电类属性的发生率
                .put(HoloAdapter())     //展示一些血条和伤害值
                .put(HealthAdapter())   //处理一些生命值事件监听
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
    fun onPlayerLoaded(event: LevelPlayerLoadedEvent) {
        asyncReloadPlayerAttrbuts(event.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onItemHeld(event: PlayerItemHeldEvent) {
        asyncReloadPlayerAttrbuts(event.player)
    }

    //判断加载的物品是否符合条件
    override fun onLoadItemAttribute(attributeData: AttributeData, itemStack: ItemStack, loadType: AttributeData.LoadType): Boolean {
        return ConditionManager.checkConditions(attributeData, itemStack, loadType)
    }

    private fun asyncReloadPlayerAttrbuts(player: Player) {
        Bukkit.getScheduler().runTaskAsynchronously(LevelPlugin.instance(), Runnable {
            var attributeData = mPlayerCache[player.uniqueId]
            if (attributeData == null) {
                attributeData = AttributeData.Builder(player)
                        .withAbility(Crit())
                        .withAbility(CritRate())
                        .withAbility(Damage())
                        .withAbility(HitRate())
                        .withAbility(Defense())
                        .withAbility(Dodge())
                        .withAbility(Health())
                        .withAbility(BlindRate())
                        .withAbility(FireRate())
                        .withLoadCallback(this)
                        .build()
                mPlayerCache[player.uniqueId] = attributeData
            }
            attributeData.reload()
        })
    }

    fun onDisable() {
        mAttributeRootAdapter.onRelease()
    }
}