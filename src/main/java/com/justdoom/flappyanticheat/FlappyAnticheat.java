package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.commands.FlagClickCommand;
import com.justdoom.flappyanticheat.commands.FlappyACCommand;
import com.justdoom.flappyanticheat.commands.tabcomplete.FlappyAnticheatTabCompletion;
import com.justdoom.flappyanticheat.data.FileData;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.utils.BrandMessageUtil;
import com.justdoom.flappyanticheat.utils.UpdateChecker;
import com.justdoom.flappyanticheat.violations.ViolationHandler;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.util.concurrent.Callable;

public final class FlappyAnticheat extends JavaPlugin {

    private static FlappyAnticheat instance;

    public static FlappyAnticheat getInstance() {
        return instance;
    }

    public ViolationHandler violationHandler;
    public PlayerDataManager dataManager;
    public FileData fileData;

    public FlappyAnticheat(){
        instance = this;
    }

    //Load PacketEvents

    @Override
    public void onLoad(){
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings
                .fallbackServerVersion(ServerVersion.v_1_16_5)
                .compatInjector(false)
                .checkForUpdates(false);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        //Check for updates

        (new UpdateChecker(this, 92180)).getVersion(version -> {
            if (!getConfig().getBoolean("disable-update-checker"))
                if (getDescription().getVersion().equalsIgnoreCase(version)) {
                    getLogger().info("There is not a new update available.");
                } else {
                    getLogger().info("There is a new update available.");
                }
        });

        saveDefaultConfig();

        //Load metrics

        int pluginId = 11300;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new Metrics.SimplePie("used_language", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getConfig().getString("language", "en");
            }
        }));

        //Register incoming plugin channel for client brand

        Messenger messenger = Bukkit.getMessenger();
        messenger.registerIncomingPluginChannel(FlappyAnticheat.getInstance(), "minecraft:brand", new BrandMessageUtil());

        //Register events
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);

        //Register commands
        this.getCommand("flappyanticheat").setExecutor(new FlappyACCommand());
        this.getCommand("flappyacflagclick").setExecutor(new FlagClickCommand());

        //Register Tab completion
        this.getCommand("flappyanticheat").setTabCompleter(new FlappyAnticheatTabCompletion());

        loadModules();

        PacketEvents.get().init();
    }

    @Override
    public void onDisable() {
        //Disable PacketEvents

        PacketEvents.get().terminate();
    }

    public void loadModules(){
        CheckManager checkManager = new CheckManager(this);
        checkManager.loadChecks();
        dataManager = new PlayerDataManager();
        fileData = new FileData();
        violationHandler = new ViolationHandler();
    }
}