package com.licrafter.mc.attribute.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by shell on 2019/9/22.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class PlayerCritEntityEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private LivingEntity entity;
    private double critValue;

    public PlayerCritEntityEvent(Player player, LivingEntity entity, double critValue) {
        this.player = player;
        this.entity = entity;
        this.critValue = critValue;
    }

    public Player getPlayer() {
        return player;
    }

    public double getCritValue() {
        return critValue;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
