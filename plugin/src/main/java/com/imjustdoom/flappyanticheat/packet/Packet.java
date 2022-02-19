package com.imjustdoom.flappyanticheat.packet;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import lombok.Getter;

@Getter
public class Packet {

    private final Direction direction;
    private final int packetId;
    private final PacketPlayReceiveEvent event;

    public Packet(Direction direction, PacketPlayReceiveEvent event, int packetId){
        this.packetId = packetId;
        this.direction = direction;
        this.event = event;
    }

    public boolean isPosition(){
        return packetId == PacketType.Play.Client.PLAYER_POSITION.getId();
    }

    public boolean isLook(){
        return packetId == PacketType.Play.Client.PLAYER_ROTATION.getId();
    }

    public boolean isPositionLook(){
        return packetId == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION.getId();
    }

    public boolean isSetting() {
        return packetId == PacketType.Play.Client.CLIENT_SETTINGS.getId();
    }

    public boolean isSteerVehicle() {
        return packetId == PacketType.Play.Client.STEER_VEHICLE.getId();
    }

    public boolean isVehicleMove() {
        return packetId == PacketType.Play.Client.VEHICLE_MOVE.getId();
    }

    //TODO: make sure it works
    public boolean isBlockPlace() {
        return packetId == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT.getId();
    }

    public boolean isIncomingTransaction () {
        return isReceiving() && packetId == PacketType.Play.Client.WINDOW_CONFIRMATION.getId();
    }

    public boolean isReceiving() {
        return direction == Direction.RECEIVE;
    }

    public boolean isSending() {
        return direction == Direction.SEND;
    }

    public boolean isFlying() {
        return isReceiving() && WrapperPlayClientPlayerFlying.isFlying(event.getPacketType());
    }

    public boolean isInventoryClick() { return packetId == PacketType.Play.Client.CLICK_WINDOW.getId(); }

    public boolean isUseEntity() {
        return isReceiving() && packetId == PacketType.Play.Client.ENTITY_ACTION.getId();
    }

    public boolean isUseItem() {
        return packetId == PacketType.Play.Client.USE_ITEM.getId();
    }

    public boolean isSlotChange() { return packetId == PacketType.Play.Client.HELD_ITEM_CHANGE.getId(); }

    // Possibly same as out pos?
    public boolean isServerPosition(){
        return packetId == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK.getId();
    }

    public boolean isOutPosition() {
        return isSending() && packetId == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK.getId();
    }

    public enum Direction { SEND, RECEIVE }
}