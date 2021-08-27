package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.listener.BukkitEventListener;
import com.justdoom.flappyanticheat.listener.NetworkListener;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.manager.CheckManager;
import com.justdoom.flappyanticheat.manager.PlayerDataManager;
import com.justdoom.flappyanticheat.packet.processor.ReceivingPacketProcessor;
import com.justdoom.flappyanticheat.util.FileUtil;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public enum FlappyAnticheat {
    INSTANCE;

    private FlappyAnticheatPlugin plugin;

    private PlayerDataManager dataManager;

    private CommentedConfigurationNode configFile;

    private final ReceivingPacketProcessor receivingPacketProcessor = new ReceivingPacketProcessor();

    private final ExecutorService packetExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService alertExecutor = Executors.newSingleThreadExecutor();

    public void start(final FlappyAnticheatPlugin plugin) {
        this.plugin = plugin;

        try {
            if(!FileUtil.doesFileExist(plugin.getDataFolder().getPath()))
                FileUtil.createDirectory(plugin.getDataFolder().getPath());

            if(!FileUtil.doesFileExist(plugin.getDataFolder() + "/config.yml"))
                FileUtil.addConfig(plugin.getDataFolder() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Paths.get(plugin.getDataFolder() + "/config.yml")) // Set where we will load and save to
                .build();

        try {
            configFile = loader.load();
            plugin.getLogger().info("Config has been loaded");
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

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new BukkitEventListener(), plugin);

        PacketEvents.get().registerListener(new NetworkListener());
    }

    public void stop(final FlappyAnticheatPlugin plugin) {
    }
}