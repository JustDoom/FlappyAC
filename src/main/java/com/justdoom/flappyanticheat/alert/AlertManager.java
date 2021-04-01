package com.justdoom.flappyanticheat.alert;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AlertManager {

    private final FlappyAnticheat plugin;

    public AlertManager(FlappyAnticheat plugin) {
        this.plugin = plugin;
    }

    public void flagMessage(Player flagger, String flag){
        String flagmsg = plugin.getConfig().getString("messages.prefix") + " " + plugin.getConfig().getString("messages.failed-check");
        flagmsg = flagmsg.replace("%player%", flagger.getName()).replace("%check%", flag);

        for(Player p: Bukkit.getOnlinePlayers()){
            if(!p.hasPermission("flappyanticheat.alerts")){
                return;
            }
            p.sendMessage(flagmsg);
        }
    }
}