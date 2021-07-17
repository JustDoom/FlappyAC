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

    private double x, y, z, lastX, lastY, lastZ;

    public PositionProcessor(FlappyPlayer player){
        this.player = player;
    }

    public void handle(double x, double y, double z, boolean onGround){
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;

        this.lastOnGround = this.onGround;

        this.x = x;
        this.y = y;
        this.z = z;

        this.onGround = onGround;

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
