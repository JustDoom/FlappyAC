package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.movement.fly.FlyA;
import com.justdoom.flappyanticheat.checks.movement.groundspoof.GroundSpoofA;
import org.bukkit.Bukkit;

public class CheckManager {

    private final FlappyAnticheat plugin;
    //private final PlayerData data;


    public CheckManager(FlappyAnticheat plugin) {
        this.plugin = plugin;
        //this.data = data;

    }

    public void loadChecks(){
        Bukkit.getServer().getPluginManager().registerEvents(new GroundSpoofA(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new FlyA(), plugin);
    }
}