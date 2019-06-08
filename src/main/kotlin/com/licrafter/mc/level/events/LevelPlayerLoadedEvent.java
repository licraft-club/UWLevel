package com.licrafter.mc.level.events;

import com.licrafter.mc.level.models.LevelPlayer;
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

    private LevelPlayer levelPlayer;
    private Player player;

    public LevelPlayerLoadedEvent(Player player, LevelPlayer levelPlayer) {
        this.player = player;
        this.levelPlayer = levelPlayer;
    }


    public Player getPlayer(){
        return player;
    }

    public LevelPlayer getLevelPlayer() {
        return levelPlayer;
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