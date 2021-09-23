package com.imjustdoom.flappyanticheat.data.processor;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import com.imjustdoom.flappyanticheat.util.type.BoundingBox;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PositionProcessor {

    private FlappyPlayer player;

    private boolean onGround, lastOnGround, mathematicallyOnGround, lastMathematicallyOnGround, inAir, inLiquid, nearPiston, nearShulker, nearVehicle, inVehicle, onLadder, lastOnLadder;

    private double x, y, z, deltaX, deltaY, deltaZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastX, lastY, lastZ,
            fallDistance, lastFallDistance, lastLastFallDistance, deltaXZ, lastDeltaXZ, lastLastDeltaY;

    private int airTicks, waterTicks, sinceVehicleTicks;

    private final List<Block> blocks = new ArrayList<>();
    private final List<Block> blocksNear = new ArrayList<>();
    private List<Entity> nearbyEntities = new ArrayList<>();

    public PositionProcessor(FlappyPlayer player) {
        this.player = player;
    }

    public void handle(double x, double y, double z, boolean onGround) {

                // Set last pos
                lastX = this.x;
                lastY = this.y;
                lastZ = this.z;

                // Set current pos
                this.x = x;
                this.y = y;
                this.z = z;

                // Set on ground
                lastOnGround = this.onGround;
                this.onGround = onGround;

                // Set mathematically onGround
                lastMathematicallyOnGround = mathematicallyOnGround;
                mathematicallyOnGround = y % (1D / 64) == 0;

                // Set last last delta Y
                lastLastDeltaY = lastDeltaY;

                // Set last delta
                lastDeltaX = deltaX;
                lastDeltaY = deltaY;
                lastDeltaZ = deltaZ;
                lastDeltaXZ = deltaXZ;

                // Set current delta
                deltaX = this.x - lastX;
                deltaY = this.y - lastY;
                deltaZ = this.z - lastZ;
                deltaXZ = Math.hypot(deltaX, deltaZ);

                // Set fall distance
                lastLastFallDistance = lastFallDistance;
                lastFallDistance = fallDistance;
                //fallDistance = player.getPlayer().getFallDistance();

            // TODO: fall distance

                // Handle collisions
                handleCollisions(0);
                handleCollisions(1);

    }

    //Credit to AntiHaxerman for this part
    public void handleCollisions(final int type) {
        blocks.clear();
        blocksNear.clear();

        final BoundingBox boundingBox = new BoundingBox(player);

        switch (type) {
            case 0:
                boundingBox.expandSpecific(0, 0, 0.55, 0.6, 0, 0);
                break;
            case 1:
                boundingBox.expandSpecific(0.1, 0.1, 0.55, 0.6, 0.1, 0.1);
                break;
        }

        final double minX = boundingBox.getMinX();
        final double minY = boundingBox.getMinY();
        final double minZ = boundingBox.getMinZ();
        final double maxX = boundingBox.getMaxX();
        final double maxY = boundingBox.getMaxY();
        final double maxZ = boundingBox.getMaxZ();

        for (double x = minX; x <= maxX; x += (maxX - minX)) {
            for (double y = minY; y <= maxY + 0.01; y += (maxY - minY) / 4) { //Expand max by 0.01 to compensate shortly for precision issues due to FP.
                for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    final Pos location = new Pos(x, y, z);
                    final Block block = this.getBlock(location, player.getPlayer().getInstance());

                    if (block != null) {
                        switch (type) {
                            case 0:
                                blocks.add(block);
                                break;
                            case 1:
                                blocksNear.add(block);
                                break;
                        }
                    }
                }
            }
        }

        switch (type) {
            case 0:
                inAir = true;
                nearShulker = false;
                nearVehicle = false;
                nearPiston = false;

                handleNearbyEntities(player.getPlayer().getInstance());

                for (final Block block : blocks) {
                    final Material material = block.registry().material();

                    inLiquid |= block.isLiquid();

                    if (material != Material.AIR) inAir = false;
                    nearPiston |= material == Material.PISTON
                            || material == Block.PISTON_HEAD.registry().material();

                    nearShulker |= material == Material.SHULKER_BOX
                            || material == Material.BLUE_SHULKER_BOX
                            || material == Material.PINK_SHULKER_BOX
                            || material == Material.BLACK_SHULKER_BOX
                            || material == Material.WHITE_SHULKER_BOX
                            || material == Material.PURPLE_SHULKER_BOX
                            || material == Material.CYAN_SHULKER_BOX
                            || material == Material.YELLOW_SHULKER_BOX
                            || material == Material.ORANGE_SHULKER_BOX
                            || material == Material.GREEN_SHULKER_BOX
                            || material == Material.LIME_SHULKER_BOX
                            || material == Material.BROWN_SHULKER_BOX
                            || material == Material.GRAY_SHULKER_BOX
                            || material == Material.LIGHT_BLUE_SHULKER_BOX
                            || material == Material.LIGHT_GRAY_SHULKER_BOX
                            || material == Material.RED_SHULKER_BOX
                            || material == Material.MAGENTA_SHULKER_BOX;
                }
                break;
            case 1:
        }

        handleClimbableCollision();
        handleTicks();
    }

    public void handleTicks(){
        airTicks = inAir ? airTicks + 1 : 0;
        waterTicks = inLiquid ? waterTicks + 1 : 0;
        inVehicle = player.getPlayer().getVehicle() != null;
        sinceVehicleTicks = inVehicle ? 0 : sinceVehicleTicks + 1;
    }

    public int floor(double num) {
        int floor = (int)num;
        return (double)floor == num ? floor : floor - (int)(Double.doubleToRawLongBits(num) >>> 63);
    }

    public void handleClimbableCollision() {
        final Pos location = player.getPlayer().getPosition();
        final int x = floor(location.x());
        final int y = floor(location.y());
        final int z = floor(location.z());
        final Block block = this.getBlock(new Pos(x, y, z), player.getPlayer().getInstance());
        lastOnLadder = onLadder;
        onLadder = block.registry().material() == Material.LADDER || block.registry().material() == Material.VINE;
    }


    public Block getBlock(final Pos location, final Instance instance) {
        if (instance.isChunkLoaded(location.blockX() >> 4, location.blockZ() >> 4)) {
            return instance.getBlock(location);
        } else {
            return null;
        }
    }

    public void handleNearbyEntities(final Instance instance) {
        try {
            nearbyEntities = PlayerUtil.getEntitiesWithinRadius(player.getPlayer().getPosition(), 2, instance);

            if (nearbyEntities == null) {
                nearVehicle = false;
                return;
            }

            for (final Entity nearbyEntity : nearbyEntities) {
                nearVehicle |= nearbyEntity.getEntityType() == EntityType.BOAT;
                nearShulker |= nearbyEntity.getEntityType() == EntityType.SHULKER;
            }
        } catch (final Throwable ignored) {

        }
    }
}
