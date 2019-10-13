package com.licrafter.mc.attribute.adapters

import com.licrafter.mc.attribute.base.adapter.AttributeDefaultAdapter
import com.licrafter.mc.attribute.base.context.abilities.*
import com.licrafter.mc.attribute.events.PlayerCritEntityEvent
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*

/**
 * Created by shell on 2019/9/15.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class DamageAdapter : AttributeDefaultAdapter() {

    override fun onIntercept(): Boolean {
        //命中几率-闪避几率-攻击/暴击-破甲几率-防御

        //计算命中几率
        val hitRateAttr = getContext()?.getAttackerAttrData()?.getAbility(HitRate::class.java) ?: 0

        val shouldCancelDodge = Random().nextInt(100) < hitRateAttr

        //计算被攻击者是否闪避
        if (!shouldCancelDodge) {
            val dodgeAttr = getContext()?.getEntityAttrData()?.getAbility(Dodge::class.java) ?: 0
            val shouldDodge = Random().nextInt(100) < dodgeAttr
            if (shouldDodge) {
                getContext()?.setDamage(0.0)
                getContext()?.cancelEvent()
                getContext()?.sendMessageToEntity("闪避成功")
                return true
            }
        }

        //如果攻击者没有RPG属性不对伤害做任何处理
        val attackerAttrs = getContext()?.getAttackerAttrData() ?: return false
        //如果没有攻击属性不对伤害做处理
        val damageAttr = attackerAttrs.getAbility(Damage::class.java)
        val critRateAttr = attackerAttrs.getAbility(CritRate::class.java)
        val critAttr = attackerAttrs.getAbility(Crit::class.java)

        var damageAdd = damageAttr
        if (Random().nextInt(100) < critRateAttr) {
            damageAdd += critAttr
            getContext()?.sendMessageToAttacker("暴击了伤害")
            val attacker = getContext()?.attacker
            val entity = getContext()?.entity
            if (attacker is Player && entity is LivingEntity) {
                Bukkit.getServer().pluginManager.callEvent(PlayerCritEntityEvent(attacker, entity, damageAdd.toDouble()))
            }
        }
        getContext()?.addDamage(damageAdd)

        //计算破甲几率
        val armorBreakAttr = attackerAttrs.getAbility(ArmorBreakRate::class.java)
        val shouldArmorBreak = Random().nextInt(100) < armorBreakAttr


        //计算被攻击者的防御
        val defenseAttr = getContext()?.getEntityAttrData()?.getAbility(Defense::class.java) ?: 0
        if (defenseAttr != 0 && !shouldArmorBreak) {
            getContext()?.reduceDamage(defenseAttr)
            getContext()?.sendMessageToEntity("防御了伤害")
        }

        //计算最后剩余伤害的反射伤害
        val context = getContext() ?: return false
        if (context.getDamage() > 0 && context.entity is Player) {
            val reflexAttr = context.getEntityAttrData()?.getAbility(Reflex::class.java) ?: return false
            val reflexRateAttr = context.getEntityAttrData()?.getAbility(ReflexRate::class.java) ?: return false

            if (Random().nextInt(100) < reflexRateAttr) {
                val reflexDamage = context.entityDamageByEntityEvent.damage / 100 * reflexAttr
                (context.entity as Player).damage(reflexDamage, context.attacker)
                context.sendMessageToAttacker("被反射伤害")
            }
        }

        return super.onIntercept()
    }
}