package com.imjustdoom.flappyanticheat.data.processor;

import com.cryptomorin.xseries.XMaterial;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import com.imjustdoom.flappyanticheat.util.type.BoundingBox;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Shulker;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PositionProcessor {

    private FlappyPlayer player;

    private boolean onGround, lastOnGround, mathematicallyOnGround, lastMathematicallyOnGround, inAir, inLiquid,
            nearPiston, nearShulker, nearVehicle, inVehicle, onLadder, lastOnLadder, teleporting, nearSlime;

    private double x, y, z, deltaX, deltaY, deltaZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastX, lastY, lastZ, groundTicks,
            fallDistance, lastFallDistance, lastLastFallDistance, deltaXZ, lastDeltaXZ, lastLastDeltaY, sinceSlimeTicks;

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
        fallDistance = player.getPlayer().getFallDistance();

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
                    final Location location = new Location(player.getPlayer().getWorld(), x, y, z);
                    final Block block = this.getBlock(location);

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
                nearSlime = false;

                handleNearbyEntities();

                for (final Block block : blocks) {
                    final Material material = block.getType();

                    inLiquid |= block.isLiquid();

                    nearSlime |= material == XMaterial.SLIME_BLOCK.parseMaterial();

                    if (material != XMaterial.AIR.parseMaterial()) inAir = false;
                    nearPiston |= material == XMaterial.PISTON.parseMaterial()
                            || material == XMaterial.PISTON_HEAD.parseMaterial();

                    nearShulker |= material == XMaterial.SHULKER_BOX.parseMaterial()
                            || material == XMaterial.BLUE_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.PINK_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.BLACK_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.WHITE_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.PURPLE_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.CYAN_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.YELLOW_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.ORANGE_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.GREEN_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.LIME_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.BROWN_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.GRAY_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.LIGHT_BLUE_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.LIGHT_GRAY_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.RED_SHULKER_BOX.parseMaterial()
                            || material == XMaterial.MAGENTA_SHULKER_BOX.parseMaterial();
                }
                break;
            case 1:
        }

        handleClimbableCollision();
        handleTicks();
    }

    public void handleTicks() {
        airTicks = inAir ? airTicks + 1 : 0;
        waterTicks = inLiquid ? waterTicks + 1 : 0;
        groundTicks = onGround ? groundTicks + 1 : 0;
        sinceSlimeTicks = nearSlime ? sinceSlimeTicks + 1 : 0;
        inVehicle = player.getPlayer().isInsideVehicle();
        sinceVehicleTicks = inVehicle ? 0 : sinceVehicleTicks + 1;
        player.getActionProcessor().handleItemUse(false);
    }

    public void handleClimbableCollision() {
        final Location location = player.getPlayer().getLocation();
        final int x = NumberConversions.floor(location.getX());
        final int y = NumberConversions.floor(location.getY());
        final int z = NumberConversions.floor(location.getZ());
        final Block block = this.getBlock(new Location(location.getWorld(), x, y, z));
        lastOnLadder = onLadder;
        onLadder = block.getType() == Material.LADDER || block.getType() == Material.VINE;
    }


    public Block getBlock(final Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getWorld().getBlockAt(location);
        } else {
            return null;
        }
    }

    public void handleNearbyEntities() {
        try {
            nearbyEntities = PlayerUtil.getEntitiesWithinRadius(player.getPlayer().getLocation(), 2);

            if (nearbyEntities == null) {
                nearVehicle = false;
                return;
            }

            for (final Entity nearbyEntity : nearbyEntities) {
                nearVehicle |= nearbyEntity instanceof Boat;
                nearShulker |= nearbyEntity instanceof Shulker;
            }
        } catch (final Throwable ignored) {

        }
    }
}
