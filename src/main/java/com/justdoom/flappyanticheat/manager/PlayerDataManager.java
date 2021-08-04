package com.justdoom.flappyanticheat.manager;

import com.justdoom.flappyanticheat.data.FlappyPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    public Map<Player, FlappyPlayer> dataMap = new ConcurrentHashMap<>();

    public ArrayList<Player> alertsDisabled = new ArrayList<Player>();

    public void addPlayer(Player player) {
        dataMap.put(player, new FlappyPlayer(player));
    }

    public boolean containsPlayer(FlappyPlayer data){
        if(dataMap.containsValue(data)){
            return true;
        } else {
            return false;
        }
    }

    public void removePlayer(Player player) {
        dataMap.remove(player);
    }

    public FlappyPlayer getData(Player player) {
        return dataMap.get(player);
    }

    public void disabledAlertsAdd(Player player){
        alertsDisabled.add(player);
    }

    public void disabledAlertsRemove(Player player){
        alertsDisabled.remove(player);
    }
}
