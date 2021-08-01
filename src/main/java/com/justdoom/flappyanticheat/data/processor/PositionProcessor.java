package com.justdoom.flappyanticheat.data.processor;

import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.util.PlayerUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

@Getter
public class PositionProcessor {

    private FlappyPlayer player;

    private boolean onGround, lastOnGround, inAir;

    private double x, y, z, deltaX, deltaY, deltaZ, lastX, lastY, lastZ, fallHeight, lastFallHeight, lastLastFallHeight, playerHeight, playerLastHeight;

    public PositionProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handle(double x, double y, double z, boolean onGround){
        lastX = this.x;
        lastY = this.y;
        lastZ = this.z;

        lastOnGround = this.onGround;

        this.x = x;
        this.y = y;
        this.z = z;

        this.onGround = onGround;

        deltaX = this.x - lastX;
        deltaY = this.y - lastY;
        deltaZ = this.z - lastZ;

        playerLastHeight = playerHeight;
        playerHeight = player.getPlayer().getFallDistance();

        lastLastFallHeight = lastFallHeight;
        lastFallHeight = fallHeight;

        if (deltaY < 0.0){
            fallHeight -= deltaY;
        }

        if (lastOnGround){
            fallHeight = 0.0;
        }

        for (Block block : PlayerUtil.getNearbyBlocksConfigurable(new Location(player.getPlayer().getWorld(), player.getPositionProcessor().getX(), player.getPositionProcessor().getY() - 1, player.getPositionProcessor().getZ()), 1, 0, 1)) {
            if (block.getType() != Material.AIR) {
                inAir = false;
                break;
            }
        }
    }

    public boolean isOnGround() {
        return onGround;
    }
}
