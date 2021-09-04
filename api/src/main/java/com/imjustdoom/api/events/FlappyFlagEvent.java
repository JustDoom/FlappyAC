package com.imjustdoom.api.events;

import com.imjustdoom.api.check.FlappyCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlappyFlagEvent extends Event implements Cancellable {

    //TODO: Change config when options are changed through the api?

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player flaggedPlayer;
    private boolean isCancelled, isPunishable, isLagbackable;
    private FlappyCheck check;
    private int vl, maxVl;

    public FlappyFlagEvent(Player player, FlappyCheck check) {
        super(true);
        this.flaggedPlayer = player;
        this.isCancelled = false;
        this.check = check;
        this.isPunishable = check.isPunishable();
        this.vl = check.getVl();
        this.maxVl = check.getMaxVl();
        this.isLagbackable = check.isLagbackable();
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

    public Player getFlaggedPlayer() {
        return this.flaggedPlayer;
    }

    public FlappyCheck getCheck() {
        return this.check;
    }

    public boolean isPunishable() {
        return this.isPunishable;
    }

    public void setPunishable(boolean punishable) {
        this.isPunishable = punishable;
    }

    public int getViolations() {
        return this.vl;
    }

    public void setViolations(int violations) {
        this.vl = violations;
    }

    public int getMaxViolations() {
        return this.maxVl;
    }

    public void setMaxViolations(int maxViolations) {
        this.maxVl = maxViolations;
    }

    public boolean isLagbackable() {
        return this.isLagbackable;
    }

    public void setLagbackable(boolean isLagbackable) {
        this.isLagbackable = isLagbackable;
    }
}
