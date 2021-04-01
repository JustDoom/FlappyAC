package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.combat.reach.ReachA;
import com.justdoom.flappyanticheat.checks.movement.speed.SpeedA;
import com.justdoom.flappyanticheat.alert.AlertManager;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlappyAnticheat extends JavaPlugin {

    public AlertManager msgutil = new AlertManager(this);

    @Override
    public void onLoad(){
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings.backupServerVersion(ServerVersion.v_1_8_8).checkForUpdates(true);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        //Bukkit.getPluginManager().registerEvents(new SpeedA(), this);
        PacketEvents.get().getEventManager().registerListener(new SpeedA(this));

        PacketEvents.get().init(this);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}