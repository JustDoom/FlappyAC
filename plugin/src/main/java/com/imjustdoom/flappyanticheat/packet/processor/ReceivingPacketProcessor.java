package com.imjustdoom.flappyanticheat.packet.processor;

import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;

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

        if(packet.isLook()) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            player.getRotationProcessor().handle(wrapper.getYaw(), wrapper.getPitch());
        }

        if (packet.isIncomingTransaction()) {
            final WrappedPacketInTransaction wrapper = new WrappedPacketInTransaction(packet.getRawPacket());
            player.getVelocityProcessor().handleTransaction(wrapper);
        }

        if (player.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final Check check : player.getChecks()) {
            check.handle(packet);
        }
    }
}