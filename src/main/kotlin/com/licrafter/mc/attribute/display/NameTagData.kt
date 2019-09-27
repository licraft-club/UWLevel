package com.licrafter.mc.attribute.display

import com.licrafter.mc.attribute.AttributeManager
import com.licrafter.mc.attribute.base.context.AttributeConfig
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import java.lang.StringBuilder

/**
 * Created by shell on 2019/9/22.
 * <p>
 * Gmail: shellljx@gmail.com
 */
class NameTagData(val entity: LivingEntity) {

    private val nameHealthDisplay: AttributeConfig.NameHealthDisplay? = AttributeManager.config.nameHealthDisplay
    private val maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?: 0.0
    val originCustomName = entity.customName
    private var updateTime = System.currentTimeMillis()
    private var currentHealth = 0.0
    var display: String? = null
        private set

    fun setCurentHealth(health: Double) {
        currentHealth = health
        updateDisplay()
    }

    private fun updateDisplay() {
        if (maxHealth == 0.0 || currentHealth == 0.0 || maxHealth < currentHealth || nameHealthDisplay == null) {
            return
        }
        val progress = currentHealth / maxHealth
        val loastLength = Math.round(nameHealthDisplay.maxLength * (1 - progress))
        val progressBuilder = StringBuilder()
        for (index in 0..loastLength) {
            progressBuilder.append(nameHealthDisplay.lost)
        }

        for (index in loastLength..nameHealthDisplay.maxLength) {
            progressBuilder.append(nameHealthDisplay.remain)
        }
        updateTime = System.currentTimeMillis()

        display = String.format(nameHealthDisplay.format, progressBuilder.toString(), String.format("%.1f", currentHealth), String.format("%.1f", maxHealth))
    }

    fun isExpired(): Boolean {
        return (System.currentTimeMillis() - updateTime) >= (nameHealthDisplay?.duration ?: 0) * 1000L
    }
}