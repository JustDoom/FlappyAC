package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.command.framework.CommandFramework;
import com.justdoom.flappyanticheat.command.impl.FlappyCommand;
import com.justdoom.flappyanticheat.command.impl.sub.AlertsCommand;
import com.justdoom.flappyanticheat.listener.BukkitEventListener;
import com.justdoom.flappyanticheat.listener.NetworkListener;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.manager.AlertManager;
import com.justdoom.flappyanticheat.manager.CheckManager;
import com.justdoom.flappyanticheat.manager.PlayerDataManager;
import com.justdoom.flappyanticheat.manager.TickManager;
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
    private CommandFramework commandFramework;

    private final TickManager tickManager = new TickManager();
    private AlertManager alertManager;
    private final ReceivingPacketProcessor receivingPacketProcessor = new ReceivingPacketProcessor();

    private final ExecutorService packetExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService alertExecutor = Executors.newSingleThreadExecutor();

    public void start(final FlappyAnticheatPlugin plugin) {
        this.plugin = plugin;

        // Create the config file
        try {
            if(!FileUtil.doesFileExist(plugin.getDataFolder().getPath()))
                FileUtil.createDirectory(plugin.getDataFolder().getPath());

            if(!FileUtil.doesFileExist(plugin.getDataFolder() + "/config.yml"))
                FileUtil.addConfig(plugin.getDataFolder() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set where we want to load and save the config from
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Paths.get(plugin.getDataFolder() + "/config.yml"))
                .build();

        // Load the config
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

        // Load/Register everything
        tickManager.start();
        dataManager = new PlayerDataManager();
        commandFramework = new CommandFramework(plugin);
        this.alertManager = new AlertManager();
        loadCommands();
        CheckManager.setup();

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new BukkitEventListener(), plugin);

        PacketEvents.get().registerListener(new NetworkListener());
    }

    public void loadCommands(){
        // For each command you have to create a new instance for it
        new FlappyCommand();
        new AlertsCommand();
    }

    public void stop(final FlappyAnticheatPlugin plugin) {
        tickManager.stop();
    }
}