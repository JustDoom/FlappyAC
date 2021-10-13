package com.imjustdoom.flappyanticheat.packet.processor;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.data.processor.PositionProcessor;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.in.vehiclemove.WrappedPacketInVehicleMove;
import io.github.retrooper.packetevents.packetwrappers.play.in.windowclick.WrappedPacketInWindowClick;

public class ReceivingPacketProcessor {

    /**
     * Handles incoming packets
     * @param data - Player to handle the packets for
     * @param packet - The packet to handle
     */
    public void handle(final FlappyPlayer data, Packet packet){
        if(packet.isFlying()){
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            PositionProcessor pos = data.getPositionProcessor();

            if(packet.isLook() || packet.isPositionLook()) {
                data.getRotationProcessor().handle(wrapper.getYaw(), wrapper.getPitch());
            }

            if(packet.isPosition() || packet.isPositionLook()) {
                if (wrapper.getX() != pos.getX() || wrapper.getY() != pos.getY() || wrapper.getZ() != pos.getZ())
                    data.getPositionProcessor().handle(wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.isOnGround());
            }
        }

        if(packet.isVehicleMove()) {
            final WrappedPacketInVehicleMove wrapper = new WrappedPacketInVehicleMove(packet.getRawPacket());

            data.getPositionProcessor().handle(wrapper.getX(), wrapper.getY(), wrapper.getZ(), false);
        }

        if(packet.isSetting()){
            final WrappedPacketInSettings wrapper = new WrappedPacketInSettings(packet.getRawPacket());

            data.getSettingProcessor().handle(wrapper);
        }

        if (packet.isIncomingTransaction()) {
            final WrappedPacketInTransaction wrapper = new WrappedPacketInTransaction(packet.getRawPacket());
            data.getVelocityProcessor().handleTransaction(wrapper);
        }

        if (packet.isSlotChange()) {
            final WrappedPacketInHeldItemSlot wrapper = new WrappedPacketInHeldItemSlot(packet.getRawPacket());
            data.getActionProcessor().handleSlots(wrapper.getCurrentSelectedSlot());
        }

        if (data.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final FlappyCheck check : data.getChecks()) {
            if(((Check) check).isEnabled()) ((Check) check).handle(packet);
        }
    }
}