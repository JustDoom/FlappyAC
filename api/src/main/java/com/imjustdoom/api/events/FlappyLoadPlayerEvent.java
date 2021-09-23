package com.imjustdoom.api.events;

import com.imjustdoom.api.data.FlappyPlayerAPI;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;

public class FlappyLoadPlayerEvent implements Event {

    //TODO: Change config when options are changed through the api?

    private final FlappyPlayerAPI player;

    public FlappyLoadPlayerEvent(FlappyPlayerAPI playerAPI) {
        this.player = playerAPI;
    }

    public Player getPlayer() {
        return this.player.getPlayer();
    }

    public String getClientVersion() {
        return player.getClientVersion();
    }

    public String getClientBrand() {
        return player.getClientBrand();
    }
}
