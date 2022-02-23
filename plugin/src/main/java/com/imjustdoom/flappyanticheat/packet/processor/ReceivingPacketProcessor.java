package com.imjustdoom.flappyanticheat.packet.processor;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;

public class ReceivingPacketProcessor {

    /**
     * Handles incoming packets
     * @param data - Player to handle the packets for
     * @param event - The packet to handle
     */
    public void handle(final FlappyPlayer data, PacketPlayReceiveEvent event){

        //if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            //final WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

            //PositionProcessor pos = data.getPositionProcessor();

            //if (packet.isLook() || packet.isPositionLook()) {
                // data.getRotationProcessor().handle(wrapper.getLocation().getYaw(), wrapper.getLocation().getPitch());
            //}

           // if ((packet.isPosition() || packet.isPositionLook())
                    //&& (wrapper.getLocation().getX() != pos.getX() || wrapper.getLocation().getY() != pos.getY() || wrapper.getLocation().getZ() != pos.getZ() || wrapper.isOnGround() != pos.isOnGround())) {
                //data.getPositionProcessor().handle(wrapper.getLocation().getX(), wrapper.getLocation().getY(), wrapper.getLocation().getZ(), wrapper.isOnGround());
            //}
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION) {
            WrapperPlayClientPlayerPosition wrapper = new WrapperPlayClientPlayerPosition(event);
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            WrapperPlayClientPlayerPositionAndRotation wrapper = new WrapperPlayClientPlayerPositionAndRotation(event);
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION) {
            WrapperPlayClientPlayerRotation wrapper = new WrapperPlayClientPlayerRotation(event);
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_FLYING) {
            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);
        } //else {
//            switch (event.getPacketType()) {
//                case VEHICLE_MOVE:
//                    final WrapperPlayClientVehicleMove vehicleMove = new WrapperPlayClientVehicleMove(event);
//
//                    //data.getPositionProcessor().handle(vehicleMove.getPosition().getX(), vehicleMove.getPosition().getY(), vehicleMove.getPosition().getZ(), false);
//                    break;
//                case CLIENT_SETTINGS:
//                    //final WrapperPlayClientSettings clientSettings = new WrapperPlayClientSettings(event);
//
//                    //data.getSettingProcessor().handle(clientSettings);
//                    break;
//                case HELD_ITEM_CHANGE:
//                    final WrapperPlayClientHeldItemChange heldItemChange = new WrapperPlayClientHeldItemChange(event);
//                    //TODO: made sure its current selected slot
//                    //data.getActionProcessor().handleSlots(heldItemChange.getSlot());
//                    break;
//                case ENTITY_ACTION:
//                    //data.getActionProcessor().handleItemUse(true);
//                    break;
//            }
//        }


        // TODO: transaction packet
       // if (packet.isIncomingTransaction()) {
            //final WrappedPacketInTransaction wrapper = new WrappedPacketInTransaction(packet.getEvent());
            //data.getVelocityProcessor().handleTransaction(wrapper);
        //}

        if (data.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final FlappyCheck check : data.getChecks()) {
            //if(check.isEnabled()) ((Check) check).handle(packet);
        }
    }
}