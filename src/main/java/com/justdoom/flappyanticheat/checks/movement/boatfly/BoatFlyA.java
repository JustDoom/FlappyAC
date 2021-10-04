package com.justdoom.flappyanticheat.checks.movement.boatfly;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoatFlyA extends Check {

    private Map<UUID, Double> airTicks = new HashMap<>();

    private Map<UUID, Boolean> inAir = new HashMap<>();

    private Map<UUID, Double> y = new HashMap<>();

    private Map<UUID, Double> lastY = new HashMap<>();

    public BoatFlyA(){
        super("BoatFly", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        double airTicks = this.airTicks.getOrDefault(uuid, 0.0);
        boolean inAir = this.inAir.getOrDefault(uuid, false);

        //measuring how many ticks the player has an air block below them for
        airTicks = inAir ? airTicks + 1 : 0;

        this.airTicks.put(uuid, airTicks);

        if (event.getPacketId() == PacketType.Play.Client.VEHICLE_MOVE) {

            lastY.put(uuid, y.getOrDefault(uuid, 0.0));
            y.put(uuid, player.getLocation().getY());

            //dont run the check if they have /fly on or are creative flying
            if (player.isFlying() || player.isGliding()) return;

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            //check if the blocks below the player are air blocks. not entirely accurate.
            for (Block block : PlayerUtil.getNearbyBlocksHorizontally(new Location(player.getWorld(),
                    player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ()), 1)) {
                if (block.getType() != Material.AIR) {
                    this.inAir.put(uuid, false);
                    break;
                } else {
                    this.inAir.put(uuid, true);
                }
            }

            if(player.isInsideVehicle() && airTicks > 10
                    && y.getOrDefault(uuid, 0.0) > 1 + lastY.getOrDefault(uuid, 0.0)
                    && player.getVehicle().getType() == EntityType.BOAT) {
                fail("", player);
            }
        }
    }
}