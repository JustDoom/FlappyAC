package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;

public class SpeedA extends PacketListenerDynamic {

    private final FlappyAnticheat plugin;

    public SpeedA(FlappyAnticheat plugin) {
        super(PacketEventPriority.NORMAL);
        this.plugin = plugin;
    }

    private double lastDist;
    private boolean lastOnGround;

    private Location pastLoc = null;

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        byte packetID = e.getPacketId();
        if (packetID == PacketType.Play.Client.POSITION) {
            WrappedPacketInFlying pos = new WrappedPacketInFlying(e.getNMSPacket());

            double distX = pos.getX() - e.getPlayer().getLocation().getX();
            double distZ = pos.getZ() - e.getPlayer().getLocation().getZ();
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
                    plugin.msgutil.flagMessage(e.getPlayer(), "SpeedA");
                }
            }
        }
    }
}
