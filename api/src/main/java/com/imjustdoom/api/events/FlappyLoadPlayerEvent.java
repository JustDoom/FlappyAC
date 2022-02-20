package com.imjustdoom.api.events;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.imjustdoom.api.data.FlappyPlayerAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlappyLoadPlayerEvent extends Event {

    //TODO: Change config when options are changed through the api?

    private static final HandlerList HANDLERS = new HandlerList();

    private final FlappyPlayerAPI player;

    public FlappyLoadPlayerEvent(FlappyPlayerAPI playerAPI) {
        this.player = playerAPI;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player.getPlayer();
    }

    public ClientVersion getClientVersion() {
        return player.getClientVersion();
    }

    public String getClientBrand() {
        return player.getClientBrand();
    }
}
