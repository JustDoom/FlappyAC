package com.justdoom.flappyanticheat.checks.movement.speed;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedA implements Listener {

    private double lastDist;
    private boolean lastOnGround;

    @EventHandler
    public void move(PlayerMoveEvent e){
        double distX = e.getTo().getX() - e.getFrom().getX();
        double distZ = e.getTo().getZ() - e.getFrom().getZ();
        double dist = (distX * distX) + (distZ * distZ);
        double lastDist = this.lastDist;
        this.lastDist = dist;

        boolean onGround = e.getPlayer().isOnGround();
        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;

        float friction = 0.91F;
        double shiftedLastDist = lastDist * friction;
        double equalness = dist - shiftedLastDist;
        double scaledEqualness = equalness * 138;

        if(!onGround && !lastOnGround){
            if(scaledEqualness >= 1.0){
                //Bukkit.broadcastMessage("hax");
            }
        }
    }
}
