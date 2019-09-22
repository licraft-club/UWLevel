package com.licrafter.mc.level.events;

import com.licrafter.mc.level.models.config.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by shell on 2019/6/7.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class LevelPlayerLoadedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private UserData userData;
    private Player player;

    public LevelPlayerLoadedEvent(Player player, UserData levelPlayer) {
        this.player = player;
        this.userData = levelPlayer;
    }


    public Player getPlayer(){
        return player;
    }

    public UserData getLevelPlayer() {
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