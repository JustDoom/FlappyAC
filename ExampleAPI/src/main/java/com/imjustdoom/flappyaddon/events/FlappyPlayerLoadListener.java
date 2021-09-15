package com.imjustdoom.flappyaddon.events;

import com.imjustdoom.api.events.FlappyLoadPlayerEvent;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FlappyPlayerLoadListener implements Listener {

    @EventHandler
    public void loadListener(FlappyLoadPlayerEvent event) {
        if(event.getClientVersion() == ClientVersion.v_1_10) {
            event.getPlayer().sendMessage("Why are you even on this version...");
        }
    }
}