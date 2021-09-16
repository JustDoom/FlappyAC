package com.imjustdoom.flappyanticheat.packet;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import lombok.Getter;

@Getter
public class Packet {

    private final Direction direction;
    private final NMSPacket rawPacket;
    private final byte packetId;

    public Packet(Direction direction, NMSPacket rawPacket, byte packetId){
        this.rawPacket = rawPacket;
        this.packetId = packetId;
        this.direction = direction;
    }

    public boolean isPosition(){
        return packetId == PacketType.Play.Client.POSITION;
    }

    public boolean isServerPosition(){
        return packetId == PacketType.Play.Server.POSITION;
    }

    public boolean isLook(){
        return packetId == PacketType.Play.Client.LOOK;
    }

    public boolean isPositionLook(){
        return packetId == PacketType.Play.Client.POSITION_LOOK;
    }

    public boolean isSetting() {
        return packetId == PacketType.Play.Client.SETTINGS;
    }

    public boolean isSteerVehicle() {
        return packetId == PacketType.Play.Client.STEER_VEHICLE;
    }

    public boolean isVehicleMove() {
        return packetId == PacketType.Play.Client.VEHICLE_MOVE;
    }

    public boolean isBlockPlace() {
        return PacketType.Play.Client.Util.isBlockPlace(packetId);
    }

    public boolean isIncomingTransaction () {
        return isReceiving() && packetId == PacketType.Play.Client.TRANSACTION;
    }

    public boolean isReceiving() {
        return direction == Direction.RECEIVE;
    }

    public boolean isFlying() {
        return isReceiving() && PacketType.Play.Client.Util.isInstanceOfFlying(packetId);
    }

    public boolean isInventoryClick() { return packetId == PacketType.Play.Client.WINDOW_CLICK; }

    public boolean isUseEntity() {
        return isReceiving() && packetId == PacketType.Play.Client.USE_ENTITY;
    }

    public enum Direction { SEND, RECEIVE }
}