package com.imjustdoom.flappyanticheat.manager;

import lombok.Getter;
import net.minestom.server.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AlertManager {
    @Getter
    public Set<UUID> toggledAlerts;

    public void toggleAlerts(Player player){
        if (this.toggledAlerts.contains(player.getUuid()))
            this.toggledAlerts.remove(player.getUuid());
        else
            this.toggledAlerts.add(player.getUuid());
    }

    public boolean hasAlerts(Player player){
        return this.toggledAlerts.contains(player.getUuid());
    }

    public void removeAlerts(Player player){
        if(hasAlerts(player)) this.toggledAlerts.remove(player.getUuid());
    }

    public AlertManager() {
        this.toggledAlerts = new HashSet<>();
    }
}
