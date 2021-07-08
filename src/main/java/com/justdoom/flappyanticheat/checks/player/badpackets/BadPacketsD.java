//Full credit to GladUrBad for the original check. This comes from Medusa.
//https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/check/impl/player/protocol/ProtocolF.java

package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BadPacketsD extends Check {

    private Map<UUID, Double> buffer = new HashMap<>();

    public BadPacketsD(){
        super("BadPackets", "D", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if (event.getPacketId() == PacketType.Play.Client.STEER_VEHICLE){

            WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(event.getNMSPacket());

            boolean unmount = wrapper.isDismount();
            boolean invalid = event.getPlayer().getVehicle() == null && !unmount;
            double buffer = this.buffer.getOrDefault(event.getPlayer().getUniqueId(), 0.0);

            if (invalid) {
                if (++buffer > 8) {
                    fail("buffer=" + buffer, event.getPlayer());
                    buffer /= 2;
                }
            } else {
                buffer = 0;
            }

            this.buffer.put(event.getPlayer().getUniqueId(), buffer);
        }
    }
}