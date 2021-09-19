package com.imjustdoom.flappyanticheat;

import com.imjustdoom.flappyanticheat.commands.FlappyACCommand;
import com.imjustdoom.flappyanticheat.commands.FlappyACHoverClick;
import com.imjustdoom.flappyanticheat.commands.FlappyACTabCompletion;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.listener.BukkitEventListener;
import com.imjustdoom.flappyanticheat.listener.NetworkListener;
import com.imjustdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.imjustdoom.flappyanticheat.manager.AlertManager;
import com.imjustdoom.flappyanticheat.manager.CheckManager;
import com.imjustdoom.flappyanticheat.manager.PlayerDataManager;
import com.imjustdoom.flappyanticheat.manager.TickManager;
import com.imjustdoom.flappyanticheat.metrics.Metrics;
import com.imjustdoom.flappyanticheat.packet.processor.ReceivingPacketProcessor;
import com.imjustdoom.flappyanticheat.listener.ClientBrandListener;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.messaging.Messenger;

import javax.security.auth.login.LoginException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public enum FlappyAnticheat {
    INSTANCE;

    private FlappyAnticheatPlugin plugin;

    public int configVersion = 1;

    private PlayerDataManager dataManager;
    private final TickManager tickManager = new TickManager();
    private AlertManager alertManager;
    //private JDA api;

    private final ReceivingPacketProcessor receivingPacketProcessor = new ReceivingPacketProcessor();

    private final ExecutorService packetExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService alertExecutor = Executors.newSingleThreadExecutor();

    public void start(final FlappyAnticheatPlugin plugin) {
        this.plugin = plugin;

        // Load config
        Config.load();

        //Load metrics

        int pluginId = 11300;
        Metrics metrics = new Metrics(getPlugin(), pluginId);

        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> getPlugin().getConfig().getString("language", "en")));

        // Load/Register everything
        tickManager.start();
        dataManager = new PlayerDataManager();
        this.alertManager = new AlertManager();
        CheckManager.setup();

        // Register client brand listener
        Messenger messenger = Bukkit.getMessenger();
        messenger.registerIncomingPluginChannel(FlappyAnticheat.INSTANCE.getPlugin(), "minecraft:brand", new ClientBrandListener());

        // Register events and packet listeners
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new BukkitEventListener(), plugin);

        PacketEvents.get().registerListener(new NetworkListener());

        // Register commands
        getPlugin().getCommand("flappyac").setExecutor(new FlappyACCommand());
        getPlugin().getCommand("flappyachoverclick").setExecutor(new FlappyACHoverClick());
        getPlugin().getCommand("flappyac").setTabCompleter(new FlappyACTabCompletion());

        // Connect to Discord
        //JDA jda = JDABuilder.createDefault(Config.DISCORD_BOT.TOKEN).build();
        //this.api = jda;
    }

    public void stop(final FlappyAnticheatPlugin plugin) {
        //api.shutdown();

        tickManager.stop();
    }
}