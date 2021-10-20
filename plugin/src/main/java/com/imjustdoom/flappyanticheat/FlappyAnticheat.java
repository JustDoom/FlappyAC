package com.imjustdoom.flappyanticheat;

import com.imjustdoom.flappyanticheat.commands.FlappyACCommand;
import com.imjustdoom.flappyanticheat.commands.FlappyACHoverClick;
import com.imjustdoom.flappyanticheat.commands.FlappyACTabCompletion;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.listener.*;
import com.imjustdoom.flappyanticheat.manager.AlertManager;
import com.imjustdoom.flappyanticheat.manager.CheckManager;
import com.imjustdoom.flappyanticheat.manager.PlayerDataManager;
import com.imjustdoom.flappyanticheat.manager.TickManager;
import com.imjustdoom.flappyanticheat.menu.MenuGUI;
import com.imjustdoom.flappyanticheat.metrics.Metrics;
import com.imjustdoom.flappyanticheat.packet.processor.ReceivingPacketProcessor;
import com.imjustdoom.flappyanticheat.util.DiscordWebhook;
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

    private final ReceivingPacketProcessor receivingPacketProcessor = new ReceivingPacketProcessor();

    private final ExecutorService packetExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService alertExecutor = Executors.newSingleThreadExecutor();

    DiscordWebhook webhook;

    private MenuGUI menuGUI;

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
        Bukkit.getPluginManager().registerEvents(new MenuClickListener(), plugin);

        PacketEvents.get().registerListener(new NetworkListener());

        // Register commands
        getPlugin().getCommand("flappyac").setExecutor(new FlappyACCommand());
        getPlugin().getCommand("flappyachoverclick").setExecutor(new FlappyACHoverClick());
        getPlugin().getCommand("flappyac").setTabCompleter(new FlappyACTabCompletion());

        // Register discord webhook
        webhook = new DiscordWebhook(Config.Discord.WEBHOOK);
        webhook.setContent(Config.Discord.STATUS);
        webhook.setAvatarUrl(Config.Discord.PROFILE);
        webhook.setUsername(Config.Discord.USERNAME);

        // Load GUI
        menuGUI = new MenuGUI();
    }

    public void stop(final FlappyAnticheatPlugin plugin) {
        //api.shutdown();

        tickManager.stop();
    }
}