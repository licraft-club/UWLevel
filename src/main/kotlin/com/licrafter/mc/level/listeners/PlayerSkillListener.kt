package com.licrafter.mc.level.listeners

import com.elmakers.mine.bukkit.api.event.CastEvent
import com.elmakers.mine.bukkit.api.spell.SpellResult
import com.elmakers.mine.bukkit.utility.CompatibilityUtils
import com.licrafter.lib.log.BLog
import com.licrafter.mc.level.*
import com.licrafter.mc.level.guis.SkillSelectGui
import com.licrafter.mc.level.models.LevelManager
import com.licrafter.mc.level.models.Message
import com.licrafter.mc.level.models.PlayerManager
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class PlayerSkillListener : Listener {

    @EventHandler
    fun onOpenSkillSelectGui(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            val levelPlayer = PlayerManager.getLevelPlayer(event.player) ?: return
            val handItem = event.player.inventory.itemInMainHand
            Material.COBWEB
            val id = ItemManager.getId(handItem) ?: return
            if (id == LevelManager.config.wand) {
                val skillGui = SkillSelectGui().create(levelPlayer, ItemManager.getSkill(handItem)) ?: return
                event.player.openInventory(skillGui)
            }
        }
    }

    @EventHandler
    fun onUseSkillBook(event: PlayerInteractEvent) {
        //主手下蹲右键
        if (event.player.isSneaking && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) && event.hand == EquipmentSlot.HAND) {
            val skillBook = event.player.inventory.itemInMainHand
            //判断是不是技能书
            ItemManager.getSkillBookConfig(skillBook) ?: return
            val requestLevel = ItemManager.getReqLevel(skillBook)
            val bindSkill = ItemManager.getSkill(skillBook)?.split("|") ?: return
            val levelePlayer = PlayerManager.getLevelPlayer(event.player)
            if (levelePlayer == null) {
                event.player.sendMessage("你还没有开启魔法之路，请去找 利姆露")
                return
            }
            if (requestLevel > levelePlayer.level) {
                event.player.sendMessage(String.format("该物品需要最低等级%s,你无法使用!!", requestLevel))
                return
            }

            val skillName = bindSkill[0]
            val skillLevel = bindSkill[1]
            val skillPre = levelePlayer.skillMap[skillName]
            if (skillPre == null || skillLevel.toInt() > skillPre.level) {
                //没学过||学习更高等级
                val result = if (skillBook.amount <= 1) {
                    ItemStack(Material.AIR)
                } else {
                    skillBook.amount -= 1
                    skillBook
                }
                event.player.inventory.setItemInMainHand(result)
                event.isCancelled = true

                levelePlayer.setSkillData(skillName, skillLevel.toInt())
                event.player.sendMessage(String.format("成功学习了技能%s", skillBook.itemMeta?.displayName))
            } else {
                //学习低等级的不允许
                event.player.sendMessage(String.format("你已经学习了该技能，不用重复学习!!"))
            }
        }
    }

    @EventHandler
    fun onCastMagicSkill(event: PlayerInteractEvent) {
        //挥舞手臂法杖执行技能
        if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK) {
            val wand = event.player.inventory.itemInMainHand
            if (ItemManager.getId(wand) == LevelManager.config.wand) {
                val levelPlayer = PlayerManager.getLevelPlayer(event.player)
                if (levelPlayer == null) {
                    event.player.sendMessage("你还没有开启魔法之路")
                    return
                }

                val bindSkill = ItemManager.getSkill(wand)?.split("|")
                if (bindSkill == null || bindSkill.isEmpty()) {
                    event.player.sendMessage("请先右键打开选择技能界面绑定技能")
                    return
                }

                val skillName = bindSkill[0]

                val skillLevel = try {
                    bindSkill[1].toInt()
                } catch (exception: Exception) {
                    1
                }
                if (levelPlayer.skillMap[skillName]?.level ?: 0 < skillLevel) {
                    event.player.sendMessage("无权执行这个技能，请查看法杖是否绑定了正确的技能")
                    return
                }
                val skillKey = if (skillLevel == 1) skillName else "$skillName|$skillLevel"
                val skillTemp = SkillsManager.magicAPI?.getSpellTemplate(skillKey)
                if (skillTemp == null) {
                    event.player.sendMessage("未知技能")
                    return
                }
                val needMagicPower = if (skillTemp.costs?.isEmpty() == true) {
                    0
                } else {
                    skillTemp.costs?.first()?.mana ?: 0
                }
                val newMagicPower = levelPlayer.magicPower - needMagicPower
                if (newMagicPower < 0) {
                    event.player.sendMessage("魔素太低: 需要 $needMagicPower 你还剩 ${levelPlayer.magicPower}")
                    return
                }
                val success = SkillsManager.cast(skillName, null, event.player)
                if (success) {
                    levelPlayer.magicPower = newMagicPower
                }
            }
        }
    }

    @EventHandler
    fun onSkillUpgrade(event: CastEvent) {
        val player = event.mage.player ?: return
        if (event.spellResult.isSuccess && event.spellResult == SpellResult.CAST) {
            val levelPlayer = PlayerManager.getLevelPlayer(player) ?: return
            val skillData = levelPlayer.skillMap[event.spell.key] ?: return
            val skillKey = if (skillData.level == 1) {
                event.spell.key
            } else {
                "${event.spell.key}|${skillData.level}"
            }

            val skillTemp = SkillsManager.magicAPI?.getSpellTemplate(skillKey) ?: return
            val success = levelPlayer.addSkillExp(skillTemp.spellKey.baseKey, skillTemp.earns, skillTemp.requiredUpgradeCasts.toDouble())
            if (success) {
                try {
                    val reqLevel = skillTemp.requiredUpgradePath.toInt()
                    if (levelPlayer.getSkillExp(skillTemp.spellKey.baseKey) >= skillTemp.requiredUpgradeCasts && levelPlayer.level >= reqLevel) {
                        levelPlayer.setSkillData(event.spell.key, skillData.level + 1)
                        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.0f)
                        CompatibilityUtils.sendActionBar(event.mage.player, skillTemp.name + "升级了，请重新绑定技能")
                    } else {
                        CompatibilityUtils.sendActionBar(player, String.format(Message.config.mageEarnsExp, skillTemp.earns))
                        player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.0f)
                    }
                } catch (e: Exception) {
                    BLog.info(LevelPlugin.instance(), "&4升级必须等级解析错误")
                }
            }
        }
    }
}