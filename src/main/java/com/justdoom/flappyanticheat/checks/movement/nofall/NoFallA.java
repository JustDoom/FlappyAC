package com.justdoom.flappyanticheat.checks.movement.nofall;

import com.cryptomorin.xseries.XMaterial;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@CheckInfo(check = "NoFall", checkType = "A", experimental = false, description = "Checks if the player says it's on the ground but isn't")
public class NoFallA extends Check {

    private int buffer = 0;

    public NoFallA(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        if(!packet.isPosition()
                && !packet.isLook()
                && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED)) return;

        double groundY = 0.015625;
        boolean client = player.getPositionProcessor().isOnGround(), server = player.getPositionProcessor().getY() % groundY < 0.0001;;

        if (client && !server && !PlayerUtil.isOnClimbable(player.getPlayer())) {
            if (++buffer > 1) {
                boolean boat = false, shulker = false, pistonHead = false;

                AtomicReference<List<Entity>> nearby = new AtomicReference<>();
                sync(() -> nearby.set(player.getPlayer().getNearbyEntities(1.5, 10, 1.5)));

                for (Entity entity : nearby.get()) {
                    if (entity.getType() == EntityType.BOAT && player.getPositionProcessor().getY() > entity.getLocation().getY()) {
                        boat = true;
                        break;
                    }

                    if (entity.getType() == EntityType.SHULKER && player.getPositionProcessor().getY() > entity.getBoundingBox().getMinY()) {
                        shulker = true;
                        break;
                    }
                }

                for (Block block : PlayerUtil.getNearbyBlocks(new Location(player.getPlayer().getWorld(), player.getPositionProcessor().getX(), player.getPositionProcessor().getY(), player.getPositionProcessor().getZ()), 2)) {

                    //TODO: exempt all types of shulker boxes
                    if(block.getType() == XMaterial.SHULKER_BOX.parseMaterial()){
                        shulker = true;
                        break;
                    }

                    if (block.getType() == XMaterial.PISTON_HEAD.parseMaterial()) {
                        pistonHead = true;
                        break;
                    }
                }

                if (!boat && !shulker && !pistonHead) {

                    fail("No Info");
                }
            }
        } else if (buffer > 0) buffer-=0.5;
    }
}