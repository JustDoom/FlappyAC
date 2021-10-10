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
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class FlyA extends Check {

    private Map<UUID, Double> airTicks = new HashMap<>();
    private Map<UUID, Double> buffer = new HashMap<>();
    private Map<UUID, Double> lastDeltaY = new HashMap<>();

    private Map<UUID, Boolean> inAir = new HashMap<>();

    private final Map<UUID, List<Block>> blocks = new HashMap<>();
    private final Map<UUID, List<Block>> blocksNear = new HashMap<>();

    public FlyA(){
        super("Fly", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if(PacketEvents.get().getPlayerUtils().isGeyserPlayer(event.getPlayer().getPlayer())) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            getBlocks(player, 0);
            getBlocks(player, 1);

            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());

            //dont run the check if they have /fly on or are creative flying
            if (player.isFlying() || player.isGliding() || player.hasPotionEffect(PotionEffectType.LEVITATION)) return;

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;


            final double deltaY = packet.getPosition().getY() - player.getLocation().getY();

            final double lastDeltaY = this.lastDeltaY.getOrDefault(uuid, 0.0);
            this.lastDeltaY.put(uuid, deltaY);

            //i dont believe you need all of the extra? also, math.abs is causing falses :///
            final double prediction = ((lastDeltaY - 0.08) * 0.98F);
            final double difference = Math.abs(deltaY - prediction);

            final boolean invalid = difference > 0.00001D
                    && airTicks.get(player.getUniqueId()) > 8
                    //was able to replace this all due to my new air block check LMFAO.
                    //note: this is not accurate if youre inside an enclosed space/near blocks. simply a quick fix i made
                    ;

            double buffer = this.buffer.getOrDefault(uuid, 0.0);

            if (invalid) {
                buffer += buffer < 50 ? 10 : 0;
                if (buffer > 20) {
                    fail("diff=%.4f, buffer=%.2f, at=%o", player);
                }
            } else {
                buffer = Math.max(buffer - 0.75, 0);
            }

            this.buffer.put(uuid, buffer);
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