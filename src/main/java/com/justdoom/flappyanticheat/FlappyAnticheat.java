package com.justdoom.flappyanticheat;

import com.justdoom.flappyanticheat.checks.CheckManager;
import com.justdoom.flappyanticheat.commands.FlagClickCommand;
import com.justdoom.flappyanticheat.commands.FlappyACCommand;
import com.justdoom.flappyanticheat.data.FileData;
import com.justdoom.flappyanticheat.data.PlayerDataManager;
import com.justdoom.flappyanticheat.events.tabcomplete.FlappyAnticheatTabCompletion;
import com.justdoom.flappyanticheat.listener.PlayerConnectionListener;
import com.justdoom.flappyanticheat.utils.UpdateChecker;
import com.justdoom.flappyanticheat.violations.ViolationHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public final class FlappyAnticheat extends JavaPlugin {

    private static FlappyAnticheat instance;

    public static FlappyAnticheat getInstance() {
        return instance;
    }

    public ViolationHandler violationHandler;
    public PlayerDataManager dataManager;
    public FileData fileData;
    private CheckManager checkManager;

    public FlappyAnticheat(){
        instance = this;
    }

    @Override
    public void onEnable() {
        (new UpdateChecker(this, 92180)).getVersion(version -> {
            if (!getConfig().getBoolean("disable-update-checker"))
                if (getDescription().getVersion().equalsIgnoreCase(version)) {
                    getLogger().info("There is not a new update available.");
                } else {
                    getLogger().info("There is a new update available.");
                }
        });

        saveDefaultConfig();

        int pluginId = 11300;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new Metrics.SimplePie("used_language", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getConfig().getString("language", "en");
            }
        }));

        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);

        this.getCommand("flappyanticheat").setExecutor(new FlappyACCommand());
        this.getCommand("flappyacpunish").setExecutor(new FlagClickCommand());
        this.getCommand("flappyanticheat").setTabCompleter(new FlappyAnticheatTabCompletion());

        loadModules();
    }

    public void loadModules(){
        checkManager = new CheckManager(this);
        dataManager = new PlayerDataManager();
        fileData = new FileData();
        violationHandler = new ViolationHandler();
        checkManager.loadChecks();
    }


}