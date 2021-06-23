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
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SpeedA extends Check implements Listener {

    private double buffer;
    private boolean lastOnGround;

    private double lastDist;

    public SpeedA() {
        super("Speed", "A", false);
    }

    @EventHandler
    public void onPacketPlayReceive(PlayerMoveEvent event) {
        Player player = event.getPlayer();



        if (ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
            return;

        Location to = new Location(player.getWorld(), event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
        float friction = 0.91f;

        double distX = to.getX() - player.getLocation().getX();
        double distZ = to.getZ() - player.getLocation().getZ();
        double dist = Math.sqrt((distX * distX) + (distZ * distZ));
        double lastDist = this.lastDist;
        this.lastDist = dist;
        boolean onGround = player.isOnGround();
        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;
        double shiftedLastDist = lastDist * friction;
        double equalness = dist - shiftedLastDist;

        if (!onGround && !lastOnGround && !(player.getNearbyEntities(1.5, 10, 1.5).size() > 0) && ++buffer > 2) {
            if (equalness > 0.027) {
                Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("e=" + equalness, player));
            }
        } else if(buffer > 0) {
            buffer -= 0.5;
        }
    }

    private float getFriction(Block block) {
        if (block == null) {
            return 0.6f * 0.91f;
        }

        switch (block.getType()) {
            case SLIME_BLOCK:
                return 0.8f * 0.91f;
            case ICE:
                return 0.98f * 0.91f;
            default:
                return 0.6f * 0.91f;
        }
    }
}
