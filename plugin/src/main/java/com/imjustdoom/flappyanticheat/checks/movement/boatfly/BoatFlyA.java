package com.imjustdoom.flappyanticheat.checks.movement.boatfly;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "BoarFly", checkType = "A", experimental = false, description = "Going up in a boat", type = CheckType.MOVEMENT)
public class BoatFlyA extends Check {

    private double stableY;

    public BoatFlyA(FlappyPlayer player) {
        super(player);
    }

    public void handle(final Packet packet) {

        //Check what packets are sent when in boat
        if (!packet.isPosition() || isExempt(ExemptType.TPS, ExemptType.GAMEMODE)) return;

        if(data.getPlayer().isInsideVehicle()
                && data.getPositionProcessor().getY() > data.getPositionProcessor().getLastY()) {
            fail("", false);
        }
    }
}