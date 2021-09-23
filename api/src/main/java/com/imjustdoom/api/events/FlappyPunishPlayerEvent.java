package com.imjustdoom.api.events;

import com.imjustdoom.api.check.FlappyCheck;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

public class FlappyPunishPlayerEvent implements Event, CancellableEvent {

    //TODO: Change config when options are changed through the api?

    private final Player punishedPlayer;
    private boolean isCancelled;
    private FlappyCheck check;

    public FlappyPunishPlayerEvent(Player player, FlappyCheck check) {
        this.punishedPlayer = player;
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

    public Player getPunishedPlayer() {
        return this.punishedPlayer;
    }

    public FlappyCheck getCheck() {
        return this.check;
    }
}
