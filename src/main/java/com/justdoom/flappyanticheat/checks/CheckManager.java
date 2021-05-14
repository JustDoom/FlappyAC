package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.movement.fly.FlyA;
import com.justdoom.flappyanticheat.checks.movement.groundspoof.GroundSpoofA;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;

public class CheckManager {

    private final FlappyAnticheat plugin;
    private final PlayerData data;


    public CheckManager(FlappyAnticheat plugin, PlayerData data) {
        this.plugin = plugin;
        this.data = data;
    }

    public void loadChecks(){
        PacketEvents.get().registerListener(new GroundSpoofA(data));
        PacketEvents.get().registerListener(new FlyA(data));
    }
}