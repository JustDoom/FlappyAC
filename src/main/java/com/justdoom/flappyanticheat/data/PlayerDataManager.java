package com.justdoom.flappyanticheat.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    public Map<UUID, PlayerData> dataMap = new HashMap<>();

    public ArrayList<Player> alertsDisabled = new ArrayList<Player>();

    public void addPlayer(UUID uuid) {
        dataMap.put(uuid, new PlayerData(uuid));
    }

    public void removePlayer(UUID uuid) {
        dataMap.remove(uuid);
    }

    public PlayerData getData(UUID uuid) {
        return dataMap.get(uuid);
    }

    public void disabledAlertsAdd(Player player){
        alertsDisabled.add(player);
    }

    public void disabledAlertsRemove(Player player){
        alertsDisabled.remove(player);
    }
}