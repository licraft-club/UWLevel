package com.licrafter.mc.attribute.adapters

import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.attribute.base.adapter.AttributeDefaultAdapter
import com.licrafter.mc.attribute.base.context.AttributeConfig
import com.licrafter.mc.attribute.base.context.AttributeContext
import com.licrafter.mc.level.LevelPlugin
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class AttributeRootAdapter : AttributeDefaultAdapter() {

    private lateinit var config: AttributeConfig
    private var attributeContext: AttributeContext? = null

    override fun onAttch(plugin: LevelPlugin) {
        this.config = AttributeManager.config
        super.onAttch(plugin)
    }

    fun onEvent(event: EntityDamageByEntityEvent) {
        var attacker = event.damager
        val entity = event.entity

        if (attacker is Projectile) {
            val shooter = attacker.shooter
            if (shooter is Player) {
                attacker = shooter
            }
        }
        if (attacker !is Player && entity !is Player) {
            return
        }
        attributeContext = AttributeContext.Builder()
                .with(event)
                .withEntity(entity)
                .withAttacker(attacker).build()
        intercept()
    }

    override fun getConfig(): AttributeConfig? {
        return config
    }

    override fun getContext(): AttributeContext? {
        return attributeContext
    }
}