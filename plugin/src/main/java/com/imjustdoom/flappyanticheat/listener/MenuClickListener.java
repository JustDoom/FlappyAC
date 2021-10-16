package com.imjustdoom.flappyanticheat.listener;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.config.Config;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;

public class MenuClickListener implements Listener {

    @EventHandler
    public void menuClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("Menu") || event.getCurrentItem() == null) return;

        event.setCancelled(true);

        String[] args = event.getCurrentItem().getItemMeta().getDisplayName().split(" ");

        ConfigurationNode section = Config.configFile.node("checks", args[0].toLowerCase(), args[1].toLowerCase());
        boolean enabled = section.node("enabled").getBoolean();
        try {
            FlappyAnticheat.INSTANCE.getMenuGUI().load((Player) event.getWhoClicked());
            section.node("enabled").set(!enabled);
            Config.saveConfig(Config.configFile);

            Config.load();

            List<FlappyPlayer> players = new ArrayList<FlappyPlayer>(FlappyAnticheat.INSTANCE.getDataManager().getPlayers().values());
            for(FlappyPlayer data : players){
                for(FlappyCheck check : data.getChecks()){
                    ((Check) check).loadConfigSection();
                }
            }
        } catch (SerializationException e) {
            event.getWhoClicked().sendMessage("Error, please check the console");
            e.printStackTrace();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }
}
