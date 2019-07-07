package com.licrafter.mc.skills.event;

import com.licrafter.mc.skills.base.context.Skill;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by shell on 2019/7/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class SkillTrueDamageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    private Player damager;
    private LivingEntity target;
    private Skill skill;
    private double damage;

    public SkillTrueDamageEvent(Player damager, LivingEntity target, double damage, Skill skill) {
        this.damager = damager;
        this.target = target;
        this.skill = skill;
        this.damage = damage;
    }

    public Player getDamager() {
        return damager;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public Skill getSkill() {
        return skill;
    }

    public double getDamage() {
        return damage;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
