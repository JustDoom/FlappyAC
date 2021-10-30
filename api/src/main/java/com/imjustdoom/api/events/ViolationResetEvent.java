package com.imjustdoom.api.events;

import com.imjustdoom.api.check.FlappyCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ViolationResetEvent extends Event implements Cancellable {

    //TODO: Change config when options are changed through the api?

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled;

    public ViolationResetEvent() {
        super(true);
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
