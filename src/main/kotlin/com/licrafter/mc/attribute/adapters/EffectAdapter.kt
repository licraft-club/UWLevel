package com.licrafter.mc.attribute.adapters

import com.licrafter.mc.attribute.base.adapter.AttributeDefaultAdapter
import com.licrafter.mc.attribute.base.context.abilities.BlindRate
import com.licrafter.mc.attribute.base.context.abilities.FireRate
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

/**
 * Created by shell on 2019/9/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class EffectAdapter : AttributeDefaultAdapter() {

    override fun onIntercept(): Boolean {

        val entity = getContext()?.entity ?: return false

        //计算点燃
        val fireRateAttr = getContext()?.getAttackerAttrData()?.getAbility(FireRate::class.java)
        if (fireRateAttr != null && Random().nextInt(100) < fireRateAttr.getAttrValue()) {
            entity.fireTicks = 100
        }

        if (entity !is Mob && entity !is Player) {
            return false
        }

        //失明
        val blindRateAttr = getContext()?.getAttackerAttrData()?.getAbility(BlindRate::class.java)
        if (blindRateAttr != null && Random().nextInt(100) < blindRateAttr.getAttrValue()) {
            addPotionEffect(entity, PotionEffectType.BLINDNESS)
        }
        return super.onIntercept()
    }

    private fun addPotionEffect(entity: Entity, type: PotionEffectType) {
        val potion = PotionEffect(type, 5, 2, true, true, true)

        if (entity is Player) {
            entity.addPotionEffect(potion, true)
        } else if (entity is Mob) {
            entity.addPotionEffect(potion, true)
        }
    }
}