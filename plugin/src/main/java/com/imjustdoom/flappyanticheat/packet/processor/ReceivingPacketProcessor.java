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

public class ReceivingPacketProcessor {

    /**
     * Handles incoming packets
     * @param data - Player to handle the packets for
     * @param packet - The packet to handle
     */
    public void handle(final FlappyPlayer data, Packet packet){

        if(WrapperPlayClientPlayerFlying.isFlying(packet.getEvent().getPacketType())){
            final WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(packet.getEvent());

            PositionProcessor pos = data.getPositionProcessor();

            if(packet.isLook() || packet.isPositionLook()) {
                data.getRotationProcessor().handle(wrapper.getLocation().getYaw(), wrapper.getLocation().getPitch());
            }

            if((packet.isPosition() || packet.isPositionLook())
                    && (wrapper.getLocation().getX() != pos.getX() || wrapper.getLocation().getY() != pos.getY() || wrapper.getLocation().getZ() != pos.getZ() || wrapper.isOnGround() != pos.isOnGround())) {
                    data.getPositionProcessor().handle(wrapper.getLocation().getX(), wrapper.getLocation().getY(), wrapper.getLocation().getZ(), wrapper.isOnGround());
            }
        } else {
            switch (packet.getEvent().getPacketType()) {
                case VEHICLE_MOVE:
                    final WrapperPlayClientVehicleMove vehicleMove = new WrapperPlayClientVehicleMove(packet.getEvent());

                    data.getPositionProcessor().handle(vehicleMove.getPosition().getX(), vehicleMove.getPosition().getY(), vehicleMove.getPosition().getZ(), false);
                    break;
                case CLIENT_SETTINGS:
                    final WrapperPlayClientSettings clientSettings = new WrapperPlayClientSettings(packet.getEvent());

                    data.getSettingProcessor().handle(clientSettings);
                    break;
                case HELD_ITEM_CHANGE:
                    final WrapperPlayClientHeldItemChange heldItemChange = new WrapperPlayClientHeldItemChange(packet.getEvent());
                    //TODO: made sure its current selected slot
                    data.getActionProcessor().handleSlots(heldItemChange.getSlot());
                    break;
                case ENTITY_ACTION:
                    data.getActionProcessor().handleItemUse(true);
                    break;
            }
        }


        // TODO: transaction packet
        if (packet.isIncomingTransaction()) {
            //final WrappedPacketInTransaction wrapper = new WrappedPacketInTransaction(packet.getEvent());
            //data.getVelocityProcessor().handleTransaction(wrapper);
        }

        if (data.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final FlappyCheck check : data.getChecks()) {
            if(check.isEnabled()) ((Check) check).handle(packet);
        }
    }
}