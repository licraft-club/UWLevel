package com.licrafter.mc.attributes.events;

import com.licrafter.mc.attributes.base.context.AttributeData;
import com.licrafter.mc.level.models.config.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by shell on 2019/9/21.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class AttributeLoadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private AttributeData attributeData;
    private UserData userData;
    private Player player;

    public AttributeLoadEvent(Player player, UserData userData, AttributeData attributeData) {
        this.player = player;
        this.userData = userData;
        this.attributeData = attributeData;
    }

    public AttributeData getAttributeData() {
        return attributeData;
    }

    public Player getPlayer() {
        return player;
    }

    public UserData getUserData() {
        return userData;
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
