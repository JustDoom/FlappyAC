package com.imjustdoom.flappyanticheat.manager;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import net.minestom.server.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    public Map<Player, FlappyPlayer> dataMap = new ConcurrentHashMap<>();

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

    public Map getPlayers() {
        return dataMap;
    }
}
