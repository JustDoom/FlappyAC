package com.justdoom.flappyanticheat.checks;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.movement.fly.FlyA;
import com.justdoom.flappyanticheat.checks.movement.groundspoof.GroundSpoofA;
import com.justdoom.flappyanticheat.checks.movement.noslow.NoSlowA;
import com.justdoom.flappyanticheat.checks.movement.speed.SpeedA;
import com.justdoom.flappyanticheat.checks.player.blockplace.BlockPlaceA;
import com.justdoom.flappyanticheat.checks.player.badpackets.BadPacketsA;
import com.justdoom.flappyanticheat.checks.player.badpackets.BadPacketsB;
import com.justdoom.flappyanticheat.checks.player.blockplace.BlockPlaceB;
import com.justdoom.flappyanticheat.checks.player.skinblinker.SkinBlinkerA;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;

public class CheckManager {

    private final FlappyAnticheat plugin;

    public CheckManager(FlappyAnticheat plugin) {
        this.plugin = plugin;
    }

    public void loadChecks(){
        PacketEvents.get().registerListener(new GroundSpoofA());
        PacketEvents.get().registerListener(new FlyA());
        PacketEvents.get().registerListener(new BadPacketsA());
        PacketEvents.get().registerListener(new BadPacketsB());
        //PacketEvents.get().registerListener(new NoSlowA());
        PacketEvents.get().registerListener(new SkinBlinkerA());
        PacketEvents.get().registerListener(new SpeedA());

        Bukkit.getPluginManager().registerEvents(new BlockPlaceA(), plugin);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceB(), plugin);
    }
}