package com.imjustdoom.flappyanticheat.checks;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "KeepSprint", checkType = "A", experimental = false, description = "", type = CheckType.MOVEMENT)
public class KeepSprintA extends Check {

    private int hitTicks;
    private double buffer;

    public KeepSprintA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isPositionLook() || packet.isPosition()) {
            final double deltaXZ = data.getFlyingProcessor().getDeltaXZ();
            final double lastDeltaXZ = data.getFlyingProcessor().getLastDeltaXZ();

            if(data.getPlayer().isSprinting() && ++hitTicks <= 2) {
                final double accel = Math.abs(deltaXZ - lastDeltaXZ);
                if(accel < 0.027) {
                    if (++buffer >= 4) {
                        fail("accel=" + accel, false);
                    }
                } else {
                    buffer = 0;
                }

            }
        } else if (packet.isUseEntity()) {
            //TODO: check if interact entity is use entity rename
            final WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(packet.getEvent());
            //Entity entity = wrapper.getEntity(data.getPlayer().getWorld());
            //if(entity.getType() == EntityType.PLAYER)
                //hitTicks = 0;
        }
    }
}