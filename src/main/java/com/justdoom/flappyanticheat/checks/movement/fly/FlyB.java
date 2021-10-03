package com.justdoom.flappyanticheat.checks.movement.fly;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.BoundingBox;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class FlyB extends Check {

    private Map<UUID, Double> stableY = new HashMap<>();

    private Map<UUID, Boolean> inAir = new HashMap<>();

    private final Map<UUID, List<Block>> blocks = new HashMap<>();
    private final Map<UUID, List<Block>> blocksNear = new HashMap<>();

    private Map<UUID, Double> airTicks = new HashMap<>();

    public FlyB(){
        super("Fly", "B", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if(PacketEvents.get().getPlayerUtils().isGeyserPlayer(event.getPlayer().getPlayer())) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());

            if(packet.getX() == player.getLocation().getX() && packet.getY() == player.getLocation().getY() && packet.getZ() == player.getLocation().getZ()) return;

            getBlocks(player, 0);
            getBlocks(player, 1);

            if (player.isFlying() || player.isGliding()) return;

            if (ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            double stableY = this.stableY.getOrDefault(uuid, 0.0);

            if(packet.getY() == player.getLocation().getY() && inAir.getOrDefault(player.getUniqueId(), false)){
                stableY++;
            } else {
                stableY = 0.0;
            }

            if(this.stableY.getOrDefault(uuid, 0.00) > 2.00) {
                fail("Air time " + this.stableY.getOrDefault(uuid, 0.00), player);
            }

            this.stableY.put(uuid, stableY);
        }
    }

    public void getBlocks(final Player player, final int type) {
        blocks.put(player.getUniqueId(), new ArrayList<>());
        blocksNear.put(player.getUniqueId(), new ArrayList<>());

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
                                List<Block> array = blocks.get(player.getUniqueId());
                                array.add(block);
                                blocks.put(player.getUniqueId(), array);
                                break;
                            case 1:
                                List<Block> array1 = blocksNear.get(player.getUniqueId());
                                array1.add(block);
                                blocksNear.put(player.getUniqueId(), array1);
                                break;
                        }
                    }
                }
            }
        }

        switch (type) {
            case 0:
                inAir.put(player.getUniqueId(), true);

                for (final Block block : blocks.getOrDefault(player.getUniqueId(), new ArrayList<>())) {
                    final Material material = block.getType();

                    if (material != Material.AIR) inAir.put(player.getUniqueId(), false);
                }
                break;
            case 1:
        }

        handleTicks(player);
    }

    public void handleTicks(Player player) {
        double airTicks = this.airTicks.getOrDefault(player.getUniqueId(), 0.0);
        boolean inAir = this.inAir.getOrDefault(player.getUniqueId(), false);

        //measuring how many ticks the player has an air block below them for
        airTicks = inAir ? airTicks + 1 : 0;

        this.airTicks.put(player.getUniqueId(), airTicks);

        //check if the blocks below the player are air blocks. not entirely accurate.
        for (Block block : PlayerUtil.getNearbyBlocksHorizontally(new Location(player.getWorld(),
                player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ()), 1)) {
            if (block.getType() != Material.AIR) {
                this.inAir.put(player.getUniqueId(), false);
                break;
            } else {
                this.inAir.put(player.getUniqueId(), true);
            }
        }
    }

    public Block getBlock(final Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getWorld().getBlockAt(location);
        } else {
            return null;
        }
    }

}