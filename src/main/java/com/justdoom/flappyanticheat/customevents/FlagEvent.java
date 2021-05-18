package com.justdoom.flappyanticheat.customevents;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private boolean isCancelled, isPunishable;
    private Check check;
    private int vl;

    public FlagEvent(Player player, Check check) {
        super(true);
        this.player = player;
        this.isCancelled = false;
        this.check = check;
        this.isPunishable = FlappyAnticheat.getInstance().getConfig().getBoolean("checks." + check.check + "." + check.checkType + ".punishable");
        this.vl = FlappyAnticheat.getInstance().violationHandler.getViolations(check, player);
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
        return this.player;
    }

    public Check getCheck(){
        return this.check;
    }

    public boolean isPunishable(){
        return this.isPunishable;
    }


    public int getViolations(){
        return this.vl;
    }
}
