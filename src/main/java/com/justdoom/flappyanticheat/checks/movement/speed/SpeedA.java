package com.justdoom.flappyanticheat.checks.movement.speed;

import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedA extends PacketListenerDynamic {

    public SpeedA() {
        super(PacketEventPriority.NORMAL);
    }

    private double lastDist;
    private boolean lastOnGround;

    private Location pastLoc = null;

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        byte packetID = e.getPacketId();
        if (packetID == PacketType.Play.Client.POSITION) {
            WrappedPacketInFlying pos = new WrappedPacketInFlying(e.getNMSPacket());
            //e.getPlayer().sendMessage("packet" + String.valueOf(pos.getX()));
            //e.getPlayer().sendMessage("player" + String.valueOf(e.getPlayer().getLocation().getX()));

            double distX = pos.getX() - this.pastLoc.getX();
            double distZ = pos.getZ() - this.pastLoc.getZ();
            double dist = (distX * distX) + (distZ * distZ);
            double lastDist = this.lastDist;
            this.lastDist = dist;

            boolean onGround = e.getPlayer().isOnGround();
            boolean lastOnGround = this.lastOnGround;
            this.lastOnGround = onGround;

            float friction = 0.91F;
            double shiftedLastDist = lastDist * friction;
            double equalness = dist - shiftedLastDist;
            double scaledEqualness = equalness * 138;

            if(!onGround && !lastOnGround){
                if(scaledEqualness >= 1.0){
                    Bukkit.broadcastMessage("hax");
                }
            }
        }
    }
}
