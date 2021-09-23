package com.imjustdoom.flappyanticheat.packet;

import lombok.Getter;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.*;

@Getter
public class Packet {

    private final Direction direction;
    private final ClientPacket rawPacket;

    public Packet(Direction direction, ClientPacket rawPacket){
        this.rawPacket = rawPacket;
        this.direction = direction;
    }

    public boolean isPosition(){
        return rawPacket instanceof ClientPlayerPositionPacket;
    }

    public boolean isServerPosition(){
        //return packetId == PacketType.Play.Server.POSITION;
        return false;
    }

    public boolean isLook(){
        return rawPacket instanceof ClientPlayerRotationPacket;
    }

    public boolean isPositionLook(){
        return rawPacket instanceof ClientPlayerPositionAndRotationPacket;
    }

    public boolean isSetting() {
        return rawPacket instanceof ClientSettingsPacket;
    }

    public boolean isSteerVehicle() {
        return rawPacket instanceof ClientSteerVehiclePacket;
    }

    public boolean isVehicleMove() {
        return rawPacket instanceof ClientVehicleMovePacket;
    }

    public boolean isBlockPlace() {
        return rawPacket instanceof ClientPlayerBlockPlacementPacket;
    }

    public boolean isIncomingTransaction () {
        //return isReceiving() && packetId == PacketType.Play.Client.TRANSACTION;
        return false;
    }

    public boolean isReceiving() {
        return direction == Direction.RECEIVE;
    }

    public boolean isInventoryClick() { return rawPacket instanceof ClientClickWindowPacket; }

    // Could be EntityInteract packet
    public boolean isUseEntity() {
        return isReceiving() && rawPacket instanceof ClientEntityActionPacket;
    }

    public enum Direction { SEND, RECEIVE }
}