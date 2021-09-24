package com.imjustdoom.flappyanticheat.checks.movement.boatfly;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.vehiclemove.WrappedPacketInVehicleMove;
import org.bukkit.entity.EntityType;

@CheckInfo(check = "BoatFly", checkType = "B", experimental = true, description = "Floating and players Y staying the same in a boat", type = CheckType.MOVEMENT)
public class BoatFlyB extends Check {

    private double stableY;

    public BoatFlyB(FlappyPlayer player) {
        super(player);
    }

    public void handle(final Packet packet) {

        if (!packet.isVehicleMove() || isExempt(ExemptType.TPS, ExemptType.GAMEMODE)) return;

        // Checks if the Y says the same while in the air
        if (data.getPositionProcessor().getY() == data.getPositionProcessor().getLastY()
                && data.getPositionProcessor().getAirTicks() > 20) {
            this.stableY++;
        } else {
            this.stableY = 0.0D;
        }
        if (this.stableY > 2.0D)
            fail("No Info", true);
    }
}