package com.justdoom.flappyanticheat.listener;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void playerQuit(final PlayerQuitEvent event){
        FlappyAnticheat.INSTANCE.getDataManager().removePlayer(event.getPlayer());
        FlappyAnticheat.INSTANCE.getAlertManager().removeAlerts(event.getPlayer());
    }
}