package com.justdoom.flappyanticheat.customevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventTests implements Listener {

    @EventHandler
    public void flagEvent(FlagEvent event){
        System.out.println(event.isPunishable());
        //event.setCancelled(true);
    }
}