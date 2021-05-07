package com.justdoom.flappyanticheat.checks.movement.groundspoof;

import com.justdoom.flappyanticheat.checks.Check;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class GroundSpoofA extends Check implements Listener {

    private final double groundY = 0.015625;

    public GroundSpoofA(){
        super("GroundSpoof", "A", true);
    }

    @EventHandler
    protected void check(PlayerMoveEvent e){
        Player player = e.getPlayer();

        boolean client = player.isOnGround(), server = player.getLocation().getY() % groundY < 0.0001;

        if (client && !server) {
            boolean boat = false;
            boolean shulker = false;

            List<Entity> nearby = player.getNearbyEntities(1.5, 10, 1.5);

            for (Entity entity : nearby) {
                if (entity.getType() == EntityType.BOAT && player.getLocation().getY() > entity.getLocation().getY()) {
                    boat = true;
                    break;
                }

                if (entity.getType() == EntityType.SHULKER && player.getLocation().getY() > entity.getBoundingBox().getMinY()) {
                    shulker = true;
                    break;
                }
            }

            if (!boat && !shulker) {
                fail("mod=" + player.getLocation().getY() % groundY);
            }
        }
    }
}