package com.justdoom.flappyanticheat.listener;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void playerJoin(final PlayerJoinEvent event) {
        FlappyAnticheat.INSTANCE.getDataManager().addPlayer(event.getPlayer());
        FlappyAnticheat.INSTANCE.getAlertManager().toggleAlerts(event.getPlayer());
    }
}