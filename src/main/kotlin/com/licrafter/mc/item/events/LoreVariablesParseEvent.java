package com.licrafter.mc.item.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by shell on 2019/10/12.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class LoreVariablesParseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private String key;
    private String value;

    public LoreVariablesParseEvent(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
