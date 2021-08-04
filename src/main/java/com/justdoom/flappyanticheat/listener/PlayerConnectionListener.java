package com.justdoom.flappyanticheat.listener;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionListener implements Listener {

    //Use async login?
    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        FlappyAnticheat.INSTANCE.getDataManager().addPlayer(event.getPlayer());
    }
}