package com.justdoom.flappyanticheat.packet.processor;

import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

public class ReceivingPacketProcessor {

    public void handle(final FlappyPlayer player, Packet packet){
        if(packet.isPosition()){
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            player.getPositionProcessor().handle(wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.isOnGround());
        }

        if(player.getPlayer().hasPermission("flappyac.bypass")) return;

        player.getChecks().forEach(check -> check.handle(packet));
    }
}