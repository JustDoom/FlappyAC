package com.justdoom.flappyanticheat.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageUtil {

    public void flagMessage(Player flagger, String flag){
        String msg = "[FlappyAC] " + flagger.getName() + " failed " + flag;

        for(Player p: Bukkit.getOnlinePlayers()){
            if(!p.hasPermission("flappyanticheat.alerts")){
                return;
            }
            p.sendMessage(msg);
        }
    }
}