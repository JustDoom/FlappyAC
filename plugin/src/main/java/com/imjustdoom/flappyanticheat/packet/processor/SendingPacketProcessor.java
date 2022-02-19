package com.imjustdoom.flappyanticheat.packet.processor;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

public class SendingPacketProcessor {

    /**
     * Handles outgoing packets
     * @param data - Player to handle the packets for
     * @param packet - The packet to handle
     */
    public void handle(final FlappyPlayer data, Packet packet){

        //TODO: make a switch statement

        if(packet.isOutPosition()) {
            //TODO: packet out position
            //final WrappedPacketOutPosition wrapper = new WrappedPacketOutPosition(packet.getRawPacket());
            //data.getPositionProcessor().handleServerPosition(wrapper);
        }

        if (data.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final FlappyCheck check : data.getChecks()) {
            if(check.isEnabled()) ((Check) check).handle(packet);
        }
    }
}