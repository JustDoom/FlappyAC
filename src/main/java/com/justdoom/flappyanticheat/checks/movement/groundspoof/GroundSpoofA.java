package com.justdoom.flappyanticheat.checks.movement.groundspoof;

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
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class GroundSpoofA extends Check {

    private int buffer = 0;

    private int justJoined = 0;

    public GroundSpoofA(){
        super("GroundSpoof", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        Player player = e.getPlayer();

        if (e.getPacketId() == PacketType.Play.Client.POSITION || e.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()) || player.getLocation().getY() < 1 || player.isDead())
                return;

            if(justJoined < 100){
                justJoined += 1;
                return;
            }

            double groundY = 0.015625;
            boolean client = packet.isOnGround(), server = packet.getY() % groundY < 0.0001;

            if (client != server && !PlayerUtil.isOnClimbable(player)) {
                if (++buffer > 1) {

                    boolean boat = false;
                    boolean shulker = false;
                    boolean pistonHead = false;

                    AtomicReference<List<Entity>> nearby = new AtomicReference<>();
                    sync(() -> nearby.set(player.getNearbyEntities(1.5, 10, 1.5)));

                    for (Entity entity : nearby.get()) {
                        if (entity.getType() == EntityType.BOAT && player.getLocation().getY() > entity.getLocation().getY()) {
                            boat = true;
                            break;
                        }

                        if (entity.getType() == EntityType.SHULKER && player.getLocation().getY() > entity.getBoundingBox().getMinY()) {
                            shulker = true;
                            break;
                        }
                    }

                    for (Block block : PlayerUtil.getNearbyBlocks(new Location(player.getWorld(), packet.getX(), packet.getY(), packet.getZ()), 2)) {

                        if (Tag.SHULKER_BOXES.isTagged(block.getType())) {
                            shulker = true;
                            break;
                        }

                        if (block.getType() == Material.PISTON_HEAD) {
                            pistonHead = true;
                            break;
                        }
                    }

                    if (!boat && !shulker && !pistonHead) {
                        String suspectedHack;
                        if(packet.getY() % groundY == 0.0){
                            suspectedHack = "Criticals/Anti Hunger";
                        } else {
                            suspectedHack = "NoFall";
                        }
                        fail("mod=" + packet.getY() % groundY + " &7Client: &2" + client + " &7Server: &2" + server + " &7Suspected Hack: &2" + suspectedHack, player);
                    }
                }
            } else if (buffer > 0) buffer--;
        }
    }
}