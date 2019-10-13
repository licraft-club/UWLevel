package com.licrafter.mc.attribute.base.context

import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.attribute.base.context.abilities.IAttribute
import com.licrafter.mc.level.models.Message
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class AttributeContext private constructor() {

    lateinit var entityDamageByEntityEvent: EntityDamageByEntityEvent
        private set
    lateinit var attacker: Entity
        private set
    lateinit var entity: Entity
        private set

    fun getEntityAttrData(): AttributeData? {
        return AttributeManager.mPlayerCache[entity.uniqueId]
    }

    fun getAttackerAttrData(): AttributeData? {
        return AttributeManager.mPlayerCache[attacker.uniqueId]
    }

    fun addDamage(amount: Int) {
        entityDamageByEntityEvent.damage += amount
    }

    fun reduceDamage(amount: Int) {
        entityDamageByEntityEvent.damage -= amount
        if (entityDamageByEntityEvent.damage < 0) {
            entityDamageByEntityEvent.damage = 0.0
        }
    }


    fun setDamage(amount: Double) {
        entityDamageByEntityEvent.damage = amount
    }

    fun getDamage(): Double {
        return entityDamageByEntityEvent.finalDamage
    }

    fun cancelEvent() {
        entityDamageByEntityEvent.isCancelled = true
    }

    fun sendMessageToEntity(message: String, vararg args: Any) {
        if (entity is Player) {
            Message.sendMessage(entity as Player, message, *args)
        }
    }

    fun sendMessageToAttacker(message: String, vararg args: Any) {
        if (attacker is Player) {
            Message.sendMessage(attacker as Player, message, *args)
        }
    }


    class Builder {
        private val context = AttributeContext()

        fun with(event: EntityDamageByEntityEvent): Builder {
            context.entityDamageByEntityEvent = event
            context.attacker = event.damager
            context.entity = event.entity
            return this
        }

        fun withEntity(entity: Entity): Builder {
            context.entity = entity
            return this
        }

        fun withAttacker(attacker: Entity): Builder {
            context.attacker = attacker
            return this
        }

        fun build(): AttributeContext {
            return context
        }
    }
}