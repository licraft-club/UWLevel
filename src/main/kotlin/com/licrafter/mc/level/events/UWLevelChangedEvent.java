package com.licrafter.mc.level.events;

import com.licrafter.mc.level.models.LevelPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by shell on 2019/6/3.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public final class UWLevelChangedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private LevelPlayer levelPlayer;

    public UWLevelChangedEvent(LevelPlayer player) {
        levelPlayer = player;
    }

    public LevelPlayer getLevelPlayer() {
        return levelPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}