package com.justdoom.flappyanticheat.data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FlappyPlayer {

    private static Map<String, FlappyPlayer> flappyPlayers = new HashMap<String, FlappyPlayer>();

    private final Player player;

    public Player getPlayer() {
        return player;
    }

    private FlappyPlayer(Player player) {
        this.player = player;
        flappyPlayers.put(player.getName(), this);
    }

    // Return a running instance (or create a new one)
    public static FlappyPlayer getInstance(Player player) {
        if (flappyPlayers == null) {
            return new FlappyPlayer(player);
        }
        if (!flappyPlayers.containsKey(player.getName())) {
            return new FlappyPlayer(player);
        } else if (flappyPlayers.containsKey(player.getName())) {
            return flappyPlayers.get(player.getName());
        } else {
            return null;
        }
    }

    // Remove an Instance
    public static boolean removeInstance(Player player) {
        if (flappyPlayers.containsKey(player.getName())) {
            flappyPlayers.remove(player.getName());
            return true;
        } else {
            return false;
        }
    }
}