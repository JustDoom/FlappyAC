package com.imjustdoom.flappyaddon.events;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.imjustdoom.api.events.FlappyLoadPlayerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FlappyPlayerLoadListener implements Listener {

    @EventHandler
    public void loadListener(FlappyLoadPlayerEvent event) {
        if(event.getClientVersion().isNewerThan(ClientVersion.V_1_10)) {
            event.getPlayer().sendMessage("Why are you even on this version...");
        }

        if(event.getClientBrand().equalsIgnoreCase("vanilla")) {
            event.getPlayer().sendMessage("You are on the " + event.getClientBrand() + " client but hack clients can spoof this");
        }
    }
}