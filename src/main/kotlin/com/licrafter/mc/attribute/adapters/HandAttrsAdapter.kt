package com.licrafter.mc.attribute.adapters

import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.attribute.base.adapter.AttributeDefaultAdapter
import com.licrafter.mc.level.models.Message
import com.licrafter.mc.level.models.PlayerManager
import com.licrafter.mc.level.utils.LoresUtil
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Created by shell on 2019/10/13.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class HandAttrsAdapter : AttributeDefaultAdapter() {

    val typeRegex = AttributeManager.config.conditionMap["Type"]?.display ?: "Type"
    val handRegex = AttributeManager.config.conditionMap["Hand"]?.display ?: "Hand"
    val ownerRegex = AttributeManager.config.conditionMap["Owner"]?.display ?: "Owner"
    val reqLevelRegex = AttributeManager.config.conditionMap["Level"]?.display ?: "Level"

    override fun onIntercept(): Boolean {
        val attacker = getContext()?.attacker ?: return true
        if (attacker is Player) {
            val attackerAttrData = getContext()?.getAttackerAttrData() ?: return false
            val mainHandItem = attacker.inventory.itemInMainHand
            if (mainHandItem.type != Material.AIR && mainHandItem.itemMeta?.hasLore() == true) {
                attackerAttrData.resetHandWeaponAbility()
                mainHandItem.itemMeta?.lore?.forEach { lore ->
                    //包含类型但是不包含手持,取消攻击
                    if (lore.contains(typeRegex) && !lore.contains(handRegex)) {
                        getContext()?.cancelEvent()
                        Message.sendMessage(attacker, "该物品不支持手持使用")
                        return true
                    }
                    //包含owner但是不满足条件
                    if (lore.contains(ownerRegex) && !lore.contains(attacker.name)) {
                        getContext()?.cancelEvent()
                        Message.sendMessage(attacker, "不是拥有者，无权使用该物品")
                        return true
                    }
                    //包含等级但是不满足条件
                    if (lore.contains(reqLevelRegex)) {
                        val reqLevel = LoresUtil.parseAttrValue(lore)
                        val attackerLevel = PlayerManager.getLevelPlayer(attacker)?.level ?: 0
                        if (reqLevel > attackerLevel) {
                            getContext()?.cancelEvent()
                            Message.sendMessage(attacker, "等级不够，无法使用该物品")
                            return true
                        }
                    }
                    attackerAttrData.loadHandWeaponAttribute(lore)
                }
            }
        }
        val entity = getContext()?.entity
        if (entity is Player) {
            val entityAttrData = getContext()?.getEntityAttrData() ?: return false
            val mainhandItem = entity.inventory.itemInMainHand
            if (mainhandItem.type != Material.AIR && mainhandItem.itemMeta?.hasLore() == true) {
                entityAttrData.resetHandWeaponAbility()
                mainhandItem.itemMeta?.lore?.forEach { lore ->
                    var shouldLoadAttr = true
                    //包含类型但是不包含手持，不读取属性
                    if (lore.contains(typeRegex) && !lore.contains(handRegex)) {
                        shouldLoadAttr = false
                    }
                    //包含owner但不满足条件，不读取属性
                    if (lore.contains(ownerRegex) && !lore.contains(entity.name)) {
                        shouldLoadAttr = false
                    }
                    //包含等级但是不满足条件，不读取属性
                    if (lore.contains(reqLevelRegex)) {
                        val reqLevel = LoresUtil.parseAttrValue(lore)
                        val entityLevel = PlayerManager.getLevelPlayer(entity)?.level ?: 0
                        if (reqLevel > entityLevel) {
                            shouldLoadAttr = false
                        }
                    }
                    if (shouldLoadAttr) {
                        entityAttrData.loadHandWeaponAttribute(lore)
                    }
                }
            }
        }
        return super.onIntercept()
    }
}