package com.imjustdoom.flappyanticheat.packet.processor;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientVehicleMove;
import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

public class ReceivingPacketProcessor {

    /**
     * Handles incoming packets
     * @param data - Player to handle the packets for
     * @param event - The packet to handle
     */
    public void handle(final FlappyPlayer data, PacketPlayReceiveEvent event){

        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            final WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

            System.out.println("boat?");

            data.getFlyingProcessor().handle(wrapper);
        } else {
            switch (event.getPacketType()) {
                case VEHICLE_MOVE:
                    final WrapperPlayClientVehicleMove vehicleMove = new WrapperPlayClientVehicleMove(event);

                    boolean posChanged = (data.getFlyingProcessor().getX() != data.getFlyingProcessor().getLastX()
                            || data.getFlyingProcessor().getY() != data.getFlyingProcessor().getLastY()
                            || data.getFlyingProcessor().getZ() != data.getFlyingProcessor().getLastZ());
                    boolean rotChanged = (data.getFlyingProcessor().getPitch() != data.getFlyingProcessor().getLastPitch()
                            || data.getFlyingProcessor().getYaw() != data.getFlyingProcessor().getLastYaw());
                    WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(posChanged, rotChanged, false,
                            new Location(vehicleMove.getPosition().getX(),
                                    vehicleMove.getPosition().getY(),
                                    vehicleMove.getPosition().getZ(),
                                    vehicleMove.getYaw(),
                                    vehicleMove.getPitch()));
                    data.getFlyingProcessor().handle(wrapper);
                    break;
                case CLIENT_SETTINGS:
                    final WrapperPlayClientSettings clientSettings = new WrapperPlayClientSettings(event);

                    data.getSettingProcessor().handle(clientSettings);
                    break;
                case HELD_ITEM_CHANGE:
                    final WrapperPlayClientHeldItemChange heldItemChange = new WrapperPlayClientHeldItemChange(event);
                    //TODO: made sure its current selected slot
                    data.getActionProcessor().handleSlots(heldItemChange.getSlot());
                    break;
                case ENTITY_ACTION:
                    data.getActionProcessor().handleItemUse(true);
                    break;
            }
        }


        // TODO: transaction packet
       // if (packet.isIncomingTransaction()) {
            //final WrappedPacketInTransaction wrapper = new WrappedPacketInTransaction(packet.getEvent());
            //data.getVelocityProcessor().handleTransaction(wrapper);
        //}

        if (data.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final FlappyCheck check : data.getChecks()) {
            if(check.isEnabled()) ((Check) check).handle(new Packet(Packet.Direction.RECEIVE, event));
        }
    }
}