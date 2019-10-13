package com.licrafter.mc.level.guis

import com.licrafter.mc.item.ItemCreateHelper
import com.licrafter.mc.level.models.config.UserData
import com.licrafter.mc.level.SkillsManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Created by shell on 2019/6/8.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class SkillSelectGui {

    fun create(player: UserData, bindedSkill: String? = null): Inventory? {
        val size = Math.max(Math.min(Math.ceil(player.skillMap.size / 9.toDouble()).toInt() * 9, 54), 9)
        val title = "技能选择界面"

        val inventory = Bukkit.createInventory(null, size, title)
        var slot = 0

        player.skillMap.forEach { name, skillData ->
            val skill = if (skillData.level == 1) {
                name
            } else {
                "$name|${skillData.level}"
            }
            val skillTemp = SkillsManager.magicAPI?.getSpellTemplate(skill) ?: return@forEach

            val itemStack = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
            val itemMeta = itemStack.itemMeta ?: return@forEach
            val bindState = if (bindedSkill != null && bindedSkill == name) "已绑定" else "未绑定"
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e${skillTemp.name} &alv.${skillData.level}"))
            val lores = arrayListOf<String>()
            lores.add("&e${skillTemp.description}")
            lores.add("&e---------->&a基本信息&e<----------")
            lores.addAll(arrayListOf(
                    "&e法杖状态: &b$bindState",
                    "&e冷却时间: &b${skillTemp.cooldown / 1000f} &e秒",
                    "&e魔法伤害: &b${skillTemp.spellParameters.getDouble("damage")} &7(单段攻击值)",
                    "&e魔法距离: &b${skillTemp.spellParameters.getDouble("range")}"
            ))
            lores.add("&e---------->&a技能升级&e<----------")
            skillTemp.requiredUpgradePath?.let { reqLevel ->
                lores.add("&e需求经验值: &b${skillData.exp}/${skillTemp.requiredUpgradeCasts}")
                lores.add("&e需求角色等级: &b$reqLevel")
            } ?: apply {
                lores.add("&4不可通过经验升级")
            }

            //lores.addAll(LoresUtil.getExtraLores(skillTemp))
            itemMeta.lore = lores.map {
                ChatColor.translateAlternateColorCodes('&', it)
            }
            itemStack.itemMeta = itemMeta
            ItemCreateHelper.setSkill(itemStack, name)
            inventory.setItem(slot, itemStack)
            slot++
        }
        return inventory
    }
}