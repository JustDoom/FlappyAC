package com.justdoom.flappyanticheat.checks.movement.fly;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlyB extends Check {

    private Map<UUID, Double> stableY = new HashMap<>();

    public FlyB(){
        super("Fly", "B", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());

            if (player.isFlying() || player.isGliding()) return;

            if (ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            double stableY = this.stableY.getOrDefault(uuid, 0.0);

            boolean inAir = true;
            for (Block block : PlayerUtil.getNearbyBlocksConfigurable(new Location(player.getWorld(), packet.getX(), packet.getY() -1, packet.getZ()), 1, 0, 1)) {
                if (block.getType() != Material.AIR) {
                    inAir = false;
                    break;
                }
            }

            if(packet.getY() == player.getLocation().getY() && inAir){
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
}