package com.justdoom.flappyanticheat.listener;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    public final FlappyAnticheat flappyAnticheat;

    public PlayerConnectionListener(FlappyAnticheat flappyAnticheat) {
        this.flappyAnticheat = flappyAnticheat;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        flappyAnticheat.dataManager.addPlayer(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        flappyAnticheat.violationHandler.clearViolations(player);
        flappyAnticheat.dataManager.removePlayer(player.getUniqueId());
    }
}