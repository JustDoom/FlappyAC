package com.imjustdoom.flappyanticheat.packet.processor;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientVehicleMove;
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

public class ReceivingPacketProcessor {

    /**
     * Handles incoming packets
     * @param data - Player to handle the packets for
     * @param packet - The packet to handle
     */
    public void handle(final FlappyPlayer data, Packet packet){

        if(packet.isFlying()){
            final WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(packet.getEvent());

            PositionProcessor pos = data.getPositionProcessor();

            if(packet.isLook() || packet.isPositionLook()) {
                data.getRotationProcessor().handle(wrapper.getLocation().getYaw(), wrapper.getLocation().getPitch());
            }

            if((packet.isPosition() || packet.isPositionLook())
                    && (wrapper.getLocation().getX() != pos.getX() || wrapper.getLocation().getY() != pos.getY() || wrapper.getLocation().getZ() != pos.getZ() || wrapper.isOnGround() != pos.isOnGround())) {
                    data.getPositionProcessor().handle(wrapper.getLocation().getX(), wrapper.getLocation().getY(), wrapper.getLocation().getZ(), wrapper.isOnGround());
            }
        }

        if(packet.isVehicleMove()) {
            final WrapperPlayClientVehicleMove wrapper = new WrapperPlayClientVehicleMove(packet.getEvent());

            data.getPositionProcessor().handle(wrapper.getPosition().getX(), wrapper.getPosition().getY(), wrapper.getPosition().getZ(), false);
        }

        if(packet.isSetting()){
            final WrapperPlayClientSettings wrapper = new WrapperPlayClientSettings(packet.getEvent());

            data.getSettingProcessor().handle(wrapper);
        }

        if (packet.isIncomingTransaction()) {
            final WrappedPacketInTransaction wrapper = new WrappedPacketInTransaction(packet.getEvent());
            data.getVelocityProcessor().handleTransaction(wrapper);
        }

        if (packet.isSlotChange()) {
            final WrapperPlayClientHeldItemChange wrapper = new WrapperPlayClientHeldItemChange(packet.getEvent());
            //TODO: made sure its current selected slot
            data.getActionProcessor().handleSlots(wrapper.getSlot());
        }

        if (packet.isUseEntity()) {
            data.getActionProcessor().handleItemUse(true);
        }

        if (data.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final FlappyCheck check : data.getChecks()) {
            if(check.isEnabled()) ((Check) check).handle(packet);
        }
    }
}