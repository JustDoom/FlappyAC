package com.imjustdoom.flappyanticheat.checks.movement.boatfly;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import org.bukkit.entity.EntityType;

@CheckInfo(check = "BoatFly", checkType = "A", experimental = false, description = "Going up in a boat", type = CheckType.MOVEMENT)
public class BoatFlyA extends Check {

    public BoatFlyA(FlappyPlayer player) {
        super(player);
    }

    public void handle(final Packet packet) {

        if (!packet.isVehicleMove() || isExempt(ExemptType.TPS, ExemptType.GAMEMODE)) return;

        if(data.getPlayer().isInsideVehicle() && data.getPositionProcessor().getAirTicks() > 10
                && data.getPositionProcessor().getY() > data.getPositionProcessor().getLastY()
                && data.getPlayer().getVehicle().getType() == EntityType.BOAT) {
            fail("", false);
        }
    }
}