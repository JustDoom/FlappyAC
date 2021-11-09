package com.imjustdoom.flappyanticheat.manager;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    public Map<Player, FlappyPlayer> dataMap = new ConcurrentHashMap<>();

    public void addPlayer(Player player) {
        dataMap.put(player, new FlappyPlayer(player));
    }

    public boolean containsPlayer(FlappyPlayer data){
        return dataMap.containsValue(data);
    }

    public void removePlayer(Player player) {
        dataMap.remove(player);
    }

    public FlappyPlayer getData(Player player) {
        return dataMap.get(player);
    }

    public Map<Player, FlappyPlayer> getPlayers() {
        return dataMap;
    }
}
