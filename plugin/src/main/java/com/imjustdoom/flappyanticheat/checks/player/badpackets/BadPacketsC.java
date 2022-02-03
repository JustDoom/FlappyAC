//Full credit to GladUrBad for the original check. This comes from Medusa.
//https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/check/impl/player/protocol/ProtocolD.java

package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BadPackets", checkType = "C", experimental = false, description = "Checks for common exploit in disablers", type = CheckType.PLAYER)
public class BadPacketsC extends Check {

    public BadPacketsC(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (!packet.isSteerVehicle()) return;
        WrapperPlayClientSteerVehicle wrapper = new WrapperPlayClientSteerVehicle(packet.getEvent());

        float forwardValue = Math.abs(wrapper.getForward()), sideValue = Math.abs(wrapper.getSideways());
        boolean invalid = forwardValue > .98F || sideValue > .98F;

        if (invalid) {
            fail("", false);
        }
    }
}
