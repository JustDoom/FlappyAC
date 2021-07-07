package com.justdoom.flappyanticheat.checks.movement.jump;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JumpA extends Check {

    private Map<UUID, Double> buffer = new HashMap<>();
    private Map<UUID, Double> lastY = new HashMap<>();

    private Map<UUID, Boolean> blockAbove = new HashMap<>();

    public JumpA(){
        super("Jump", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            if (player.isFlying()) return;

            //had to skid the potion level util from medusa, sorry
            double jumpSize = 0.43f + (double) + ((float) PlayerUtil.getPotionLevel(player, PotionEffectType.JUMP) * 0.1f);

            final double deltaY = packet.getPosition().getY() - player.getLocation().getY();
            double lastY = this.lastY.getOrDefault(uuid, 0.0);

            final boolean onGround = packet.isOnGround();
            boolean jumped = deltaY > 0 && lastY % (1D/64) == 0; //&& !onGround

            for (Block block : PlayerUtil.getNearbyBlocks(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 3, player.getLocation().getZ()), 1)) {
                if (block.getType() != Material.AIR) {
                    this.blockAbove.put(uuid, true);
                    break;
                } else {
                    this.blockAbove.put(uuid, false);
                }
            }

            boolean blockAbove = this.blockAbove.getOrDefault(uuid, false);

            if (deltaY < (jumpSize - 0.02) && jumped && !blockAbove) {
                //event.getPlayer().sendMessage("youre low" + deltaY);
                fail("",player);
            }

            double buffer = this.buffer.getOrDefault(uuid, 0.0);

            if (deltaY > (onGround ? 0.6 : jumpSize)) {
                if (++buffer > 2) {
                //    event.getPlayer().sendMessage("youre high" + deltaY + " " + blockAbove);
                    fail("", player);
                }
            } else {
                buffer = Math.max(buffer - 0.5, 0);
            }

            this.buffer.put(uuid, buffer);

            lastY = packet.getPosition().getY();
            this.lastY.put(uuid, lastY);
        }
    }
}