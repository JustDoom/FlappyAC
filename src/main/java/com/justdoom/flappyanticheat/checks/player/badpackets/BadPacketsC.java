//Full credit to GladUrBad for the original check. This comes from Medusa.
//https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/check/impl/player/protocol/ProtocolD.java

package com.justdoom.flappyanticheat.checks.player.badpackets;

import com.justdoom.flappyanticheat.checks.Check;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;

public class BadPacketsC extends Check {

    public BadPacketsC(){
        super("BadPackets", "C", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if (event.getPacketId() == PacketType.Play.Client.STEER_VEHICLE){

            WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(event.getNMSPacket());

            float forwardValue = Math.abs(wrapper.getForwardValue()), sideValue = Math.abs(wrapper.getSideValue());
            boolean invalid = forwardValue > .98F || sideValue > .98F;

            if (invalid) {
                fail("", event.getPlayer());
            }
        }
    }
}