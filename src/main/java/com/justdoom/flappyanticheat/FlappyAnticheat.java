package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.listener.NetworkListener;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.packet.processor.ReceivingPacketProcessor;
import com.justdoom.flappyanticheat.util.FileUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
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

    private PlayerDataManager dataManager;

    public CommentedConfigurationNode configFile;

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

        try {
            if(!FileUtil.doesFileExist(getDataFolder().getPath()))
                FileUtil.createDirectory(getDataFolder().getPath());

            if(!FileUtil.doesFileExist(getDataFolder() + "/config.yml"))
                FileUtil.addConfig(getDataFolder() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of(getDataFolder() + "/config.yml")) // Set where we will load and save to
                .build();

        try {
            configFile = loader.load();
            getLogger().info("Config has been loaded");
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }

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