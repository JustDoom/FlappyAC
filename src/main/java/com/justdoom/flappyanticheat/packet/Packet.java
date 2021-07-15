package com.justdoom.flappyanticheat.packet;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import lombok.Getter;

@Getter
public class Packet {

    private final NMSPacket rawPacket;
    private final byte packetId;

    public Packet(NMSPacket rawPacket, byte packetId){
        this.rawPacket = rawPacket;
        this.packetId = packetId;
    }

    public boolean isPosition(){
        return packetId == PacketType.Play.Client.POSITION;
    }
}