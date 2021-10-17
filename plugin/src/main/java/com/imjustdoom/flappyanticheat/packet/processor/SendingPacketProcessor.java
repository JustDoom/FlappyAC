package com.imjustdoom.flappyanticheat.packet.processor;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.data.processor.PositionProcessor;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.in.vehiclemove.WrappedPacketInVehicleMove;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;

public class SendingPacketProcessor {

    /**
     * Handles outgoing packets
     * @param data - Player to handle the packets for
     * @param packet - The packet to handle
     */
    public void handle(final FlappyPlayer data, Packet packet){
        if(packet.isOutPosition()) {
            final WrappedPacketOutPosition wrapper = new WrappedPacketOutPosition(packet.getRawPacket());
            data.getPositionProcessor().handleServerPosition(wrapper);
        }

        if (data.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final FlappyCheck check : data.getChecks()) {
            if(((Check) check).isEnabled()) ((Check) check).handle(packet);
        }
    }
}