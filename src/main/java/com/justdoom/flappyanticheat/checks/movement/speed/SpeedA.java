package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SpeedA extends Check implements Listener {

    private Map<UUID, Double> buffer = new HashMap<>();
    private Map<UUID, Boolean> lastOnGround = new HashMap<>();

    private Map<UUID, Double> lastDist = new HashMap<>();

    public SpeedA() {
        super("Speed", "A", false);
    }

    @EventHandler
    public void onPacketPlayReceive(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
            return;

        Location to = new Location(player.getWorld(), event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
        float friction = 0.91f;

        double distX = to.getX() - player.getLocation().getX();
        double distZ = to.getZ() - player.getLocation().getZ();
        double dist = Math.sqrt((distX * distX) + (distZ * distZ));
        double lastDist = this.lastDist.getOrDefault(uuid, 0.0);

        this.lastDist.put(uuid, dist);
        boolean onGround = player.isOnGround();
        boolean lastOnGround = this.lastOnGround.getOrDefault(uuid, true);
        this.lastOnGround.put(uuid, onGround);
        double shiftedLastDist = lastDist * friction;
        double equalness = dist - shiftedLastDist;

        double bufferOrDefault = this.buffer.getOrDefault(uuid, 0.0);

        if (!PlayerUtil.isOnClimbable(player) && !onGround && !lastOnGround && !(player.getNearbyEntities(1.5, 10, 1.5).size() > 0) && this.buffer.put(uuid, ++bufferOrDefault) > 2) {

            boolean pistonHead = false;

            for (Block block : PlayerUtil.getNearbyBlocks(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()), 2)) {
                if (block.getType() == Material.PISTON_HEAD) {
                    pistonHead = true;
                    break;
                }
            }

            if (equalness > 0.027 && !pistonHead) {
                Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("e=" + equalness, player));
            }
        } else if(this.buffer.getOrDefault(uuid, 0.0) > 0) {
            bufferOrDefault = this.buffer.getOrDefault(uuid, 0.0);
            this.buffer.put(uuid, bufferOrDefault -= 0.5);
        }
    }
}
