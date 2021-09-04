package com.imjustdoom.flappyanticheat;

import com.imjustdoom.flappyanticheat.command.framework.CommandFramework;
import com.imjustdoom.flappyanticheat.command.impl.FlappyCommand;
import com.imjustdoom.flappyanticheat.command.impl.sub.AlertsCommand;
import com.imjustdoom.flappyanticheat.listener.BukkitEventListener;
import com.imjustdoom.flappyanticheat.listener.NetworkListener;
import com.imjustdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.imjustdoom.flappyanticheat.manager.AlertManager;
import com.imjustdoom.flappyanticheat.manager.CheckManager;
import com.imjustdoom.flappyanticheat.manager.PlayerDataManager;
import com.imjustdoom.flappyanticheat.manager.TickManager;
import com.imjustdoom.flappyanticheat.packet.processor.ReceivingPacketProcessor;
import com.imjustdoom.flappyanticheat.util.ClientBrandUtil;
import com.imjustdoom.flappyanticheat.util.FileUtil;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.messaging.Messenger;
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

        //Load metrics

        int pluginId = 11300;
        Metrics metrics = new Metrics(getPlugin(), pluginId);

        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> getPlugin().getConfig().getString("language", "en")));

        // Load/Register everything
        tickManager.start();
        dataManager = new PlayerDataManager();
        commandFramework = new CommandFramework();
        this.alertManager = new AlertManager();
        loadCommands();
        CheckManager.setup();

        Messenger messenger = Bukkit.getMessenger();
        messenger.registerIncomingPluginChannel(FlappyAnticheat.INSTANCE.getPlugin(), "minecraft:brand", new ClientBrandUtil());

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