package com.justdoom.flappyanticheat.packet.processor;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;

public class ReceivingPacketProcessor {

    public void handle(final FlappyPlayer player, Packet packet){
        if(packet.isPosition()){
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            player.getPositionProcessor().handle(wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.isOnGround());
        }

        if(packet.isSetting()){
            final WrappedPacketInSettings wrapper = new WrappedPacketInSettings(packet.getRawPacket());

            player.getSettingProcessor().handle(wrapper);
        }

        if (player.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final Check check : player.getChecks()) {
            check.handle(packet);
        }
    }
}