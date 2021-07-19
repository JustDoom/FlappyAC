package com.justdoom.flappyanticheat.checks.movement.groundspoof;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
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

public class GroundSpoofA extends Check implements Listener {

    private int buffer = 0;

    private boolean isLastOnGround;

    public GroundSpoofA() {
        super("GroundSpoof", "A", false);
    }

    @EventHandler
    public void check(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (player.getLocation().getY() < 1
                || player.isDead() 
                || player.isInsideVehicle()) {
            return;
        }

        double groundY = 0.015625;
        boolean client = player.isOnGround(), server = e.getTo().getY() % groundY < 0.0001;

        if (client && !server && !e.getFrom().getBlock().getType().name().contains("LADDER")
                && !e.getTo().getBlock().getType().name().contains("LADDER")
                && !e.getFrom().getBlock().getType().name().contains("VINE")
                && !e.getTo().getBlock().getType().name().contains("VINE")) {

            boolean boat = false;
            boolean shulker = false;
            boolean pistonHead = false;
            // TODO Implement better pistons
            for (Entity entity : player.getNearbyEntities(1.5, 2, 1.5)) {
                if (entity.getType() == EntityType.BOAT && player.getLocation().getY() > entity.getLocation().getY()) {
                    boat = true;
                    break;
                }

                if (entity.getType() == EntityType.SHULKER) {
                    shulker = true;
                    break;
                }
            }

            if (!boat && !shulker && !pistonHead && ++buffer > 1) {
                Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("mod=" + e.getTo().getY() % groundY + " &7Client: &2" + client + " &7Server: &2" + server, player));
            }
        } else if (buffer > 0)
            buffer -= 0.5;
    }
}