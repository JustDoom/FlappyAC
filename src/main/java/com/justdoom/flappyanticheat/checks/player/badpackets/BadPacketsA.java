package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

@CheckData(name = "BadPackets", type = "A")
public class BadPacketsA extends Check {

    public BadPacketsA(PlayerData data) {
        super(data);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());
            if(Math.abs(packet.getPitch())  > 90.0){
                fail("pitch=" + packet.getPitch(), event.getPlayer());
            }
        }
    }
}
