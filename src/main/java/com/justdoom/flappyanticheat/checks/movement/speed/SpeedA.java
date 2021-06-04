package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SpeedA extends Check {

    private int onGroundTime = 0;
    private boolean lastOnGround;

    private double lastDist;

    public SpeedA() {
        super("Speed", "A", true);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if (event.getPacketId() == PacketType.Play.Client.POSITION) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());
            Player player = event.getPlayer();

            if (ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            Location to = new Location(player.getWorld(), packet.getX(), packet.getY(), packet.getZ());
            float friction = 0.91f;

            double distX = to.getX() - player.getLocation().getX();
            double distZ = to.getZ() - player.getLocation().getZ();
            double dist = Math.sqrt((distX * distX) + (distZ * distZ));
            double lastDist = this.lastDist;
            this.lastDist = dist;
            boolean onGround = packet.isOnGround();
            boolean lastOnGround = this.lastOnGround;
            this.lastOnGround = onGround;
            double shiftedLastDist = lastDist * friction;
            double equalness = dist - shiftedLastDist;


            if(!onGround && !lastOnGround){
                if(equalness > 0.027){
                    fail("e=" + equalness, player);
                }
            }
        }
    }

    private float getFriction(Block block) {
        if(block == null) { return 0.6f * 0.91f; }

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
