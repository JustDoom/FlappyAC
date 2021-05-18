package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpeedA extends Check {

    private int onGroundTime = 0;
    private boolean lastOnGround;

    public SpeedA(){
        super("Speed", "A", true);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if(event.getPacketId() == PacketType.Play.Client.POSITION){
            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());
            Player player = event.getPlayer();

            String path = ("checks." + check + "." + checkType).toLowerCase();
            if(PacketEvents.get().getServerUtils().getTPS() <= FlappyAnticheat.getInstance().getConfig().getDouble(path + ".min-tps")){
                return;
            }

            double deltaX = packet.getX() - player.getLocation().getX();
            double deltaZ = packet.getZ() - player.getLocation().getZ();

            double deltaXZ = Math.hypot(deltaX, deltaZ);

            boolean onGround = packet.isOnGround();
            boolean lastOnGround = this.lastOnGround;
            this.lastOnGround = onGround;

            if(deltaXZ > 0.4) {
                //player.sendMessage(String.valueOf(deltaXZ));
            }

            /**double distX = packet.getX() - player.getLocation().getX();
            double distZ = packet.getZ() - player.getLocation().getZ();
            double dist = (distX * distX) + (distZ * distZ);
            double lastDist = this.lastDist;
            this.lastDist = dist;

            boolean onGround = packet.isOnGround();
            boolean lastOnGround = this.lastOnGround;
            this.lastOnGround = onGround;

            float friction = 0.91F;
            double shiftedLastDist = lastDist * friction;
            double equalness = dist - shiftedLastDist;
            double scaledEqualness = equalness * 138;

            if(!onGround && !lastOnGround){
                if(scaledEqualness >= 1.0){
                    Bukkit.broadcastMessage(String.valueOf(scaledEqualness));
                }
            }

            if(packet.isOnGround()){
                onGroundTime++;
            }

            if(onGroundTime >= 15){
                //double expected = thisMove / data.getGroundTime() + baseMove;
            }**/
        }
    }
}
