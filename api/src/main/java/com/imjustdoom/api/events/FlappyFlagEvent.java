package com.imjustdoom.api.events;

import com.imjustdoom.api.check.FlappyCheck;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

public class FlappyFlagEvent implements Event, CancellableEvent {

    //TODO: Change config when options are changed through the api?

    private final Player flaggedPlayer;
    private boolean isCancelled;
    private FlappyCheck check;

    public FlappyFlagEvent(Player player, FlappyCheck check) {
        this.flaggedPlayer = player;
        this.isCancelled = false;
        this.check = check;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Player getFlaggedPlayer() {
        return this.flaggedPlayer;
    }

    public FlappyCheck getCheck() {
        return this.check;
    }
}
