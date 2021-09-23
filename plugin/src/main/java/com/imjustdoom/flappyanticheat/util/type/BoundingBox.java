package com.imjustdoom.flappyanticheat.util.type;

import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Getter
//Credit to AntiHaxerman for this part
public final class BoundingBox {

    private double minX, minY, minZ;
    private double maxX, maxY, maxZ;

    public BoundingBox(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ) {
        if (minX < maxX) {
            this.minX = minX;
            this.maxX = maxX;
        } else {
            this.minX = maxX;
            this.maxX = minX;
        }
        if (minY < maxY) {
            this.minY = minY;
            this.maxY = maxY;
        } else {
            this.minY = maxY;
            this.maxY = minY;
        }
        if (minZ < maxZ) {
            this.minZ = minZ;
            this.maxZ = maxZ;
        } else {
            this.minZ = maxZ;
            this.maxZ = minZ;
        }
    }

    public BoundingBox(final FlappyPlayer data) {
        this.minX = data.getPositionProcessor().getX() - 0.3D;
        this.minY = data.getPositionProcessor().getY();
        this.minZ = data.getPositionProcessor().getZ() - 0.3D;
        this.maxX = data.getPositionProcessor().getX() + 0.3D;
        this.maxY = data.getPositionProcessor().getY() + 1.8D;
        this.maxZ = data.getPositionProcessor().getZ() + 0.3D;
    }

    public BoundingBox(final Vec data) {
        this.minX = data.x() - 0.4D;
        this.minY = data.y();
        this.minZ = data.z() - 0.4D;
        this.maxX = data.x() + 0.4D;
        this.maxY = data.y() + 1.9D;
        this.maxZ = data.z() + 0.4D;
    }

    public BoundingBox move(final double x, final double y, final double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public List<Block> getBlocks(final Instance world) {
        final List<Block> blockList = new ArrayList<>();

        final double minX = this.minX;
        final double minY = this.minY;
        final double minZ = this.minZ;
        final double maxX = this.maxX;
        final double maxY = this.maxY;
        final double maxZ = this.maxZ;

        for (double x = minX; x <= maxX; x += (maxX - minX)) {
            for (double y = minY; y <= maxY; y += (maxY - minY)) {
                for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    final Block block = world.getBlock(new Pos(x, y, z));
                    blockList.add(block);
                }
            }
        }
        return blockList;
    }

    public BoundingBox expand(final double x, final double y, final double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox expandSpecific(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ) {
        this.minX -= minX;
        this.minY -= minY;
        this.minZ -= minZ;

        this.maxX += maxX;
        this.maxY += maxY;
        this.maxZ += maxZ;

        return this;
    }
}