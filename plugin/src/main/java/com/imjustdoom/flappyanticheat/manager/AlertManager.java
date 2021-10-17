package com.imjustdoom.flappyanticheat.manager;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AlertManager {
    @Getter
    public Set<UUID> toggledAlerts;

    public void toggleAlerts(Player player){
        if (this.toggledAlerts.contains(player.getUniqueId()))
            this.toggledAlerts.remove(player.getUniqueId());
        else
            this.toggledAlerts.add(player.getUniqueId());
    }

    public boolean hasAlerts(Player player){
        return this.toggledAlerts.contains(player.getUniqueId());
    }

    public void removeAlerts(Player player){
        if(hasAlerts(player)) this.toggledAlerts.remove(player.getUniqueId());
    }

    public AlertManager() {
        this.toggledAlerts = new HashSet<>();
    }
}