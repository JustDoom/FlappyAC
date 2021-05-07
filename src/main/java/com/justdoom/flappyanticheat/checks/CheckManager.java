package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.movement.groundspoof.GroundSpoofA;
import com.justdoom.flappyanticheat.data.PlayerData;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    private final FlappyAnticheat plugin;
    private final PlayerData data;

    public List<Listener> listeners = new ArrayList<>();

    public CheckManager(FlappyAnticheat plugin, PlayerData data) {
        this.plugin = plugin;
        this.data = data;

        listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }




    public void loadChecks(){
        listeners.add(new GroundSpoofA(data));
    }
}