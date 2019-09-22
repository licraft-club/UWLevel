package com.licrafter.mc.level.events;

import com.licrafter.mc.level.models.config.LevelConfig;
import com.licrafter.mc.level.models.config.UserData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by shell on 2019/6/3.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public final class UWLevelUpEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    private LevelConfig.Level from;
    private LevelConfig.Level to;
    private UserData userData;

    public UWLevelUpEvent(UserData player, LevelConfig.Level from, LevelConfig.Level to) {
        this.userData = player;
        this.from = from;
        this.to = to;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
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
}
