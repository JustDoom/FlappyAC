package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.listener.NetworkListener;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.packet.processor.ReceivingPacketProcessor;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public final class FlappyAnticheat extends JavaPlugin {

    private static FlappyAnticheat instance;

    public static FlappyAnticheat getInstance() {
        return instance;
    }

    public FlappyAnticheat(){
        instance = this;
    }

    public PlayerDataManager dataManager;

    private final ReceivingPacketProcessor receivingPacketProcessor = new ReceivingPacketProcessor();
    private final ExecutorService packetExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onLoad(){
        //Load PacketEvents
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings
                .fallbackServerVersion(ServerVersion.v_1_17_1)
                .compatInjector(false)
                .checkForUpdates(false)
                .bStats(true);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        dataManager = new PlayerDataManager();
        CheckManager.setup();

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(), this);

        PacketEvents.get().registerListener(new NetworkListener());

        PacketEvents.get().init();
    }

    @Override
    public void onDisable() {
        //Disable PacketEvents
        PacketEvents.get().terminate();
    }
}