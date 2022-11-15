package com.justdoom.flappyanticheat.events;

import com.justdoom.flappyanticheat.checks.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PunishEvent extends Event implements Cancellable {

    private final Player player;
    private boolean isCancelled;
    private Check check;

    public PunishEvent(Player player, Check check) {
        super(true);
        this.player = player;
        this.isCancelled = false;
        this.check = check;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPunishedPlayer() {
        return this.player;
    }

    public Check getCheck(){
        return this.check;
    }
}
