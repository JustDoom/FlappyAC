package com.imjustdoom.flappyanticheat.checks.player.badpackets;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.data.processor.PositionProcessor;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

import java.util.Vector;

@CheckInfo(check = "BadPackets", checkType = "H", experimental = false, description = "Checks for weird pos packets", type = CheckType.PLAYER)
public class BadPacketsH extends Check {

    public BadPacketsH(FlappyPlayer data) {
        super(data);
    }

    private boolean checkNextPosition;

    @Override
    public void handle(Packet packet) {
        if (packet.isPosition()) {
            PositionProcessor pos = getData().getPositionProcessor();
            //if (pos.isTeleporting()) return;
            if (checkNextPosition) {
                if (!pos.isOnGround() && pos.getDeltaY() < 0) {
                    data.getPlayer().sendMessage("checked next position - dy=" + pos.getDeltaY() + " ldy=" + pos.getLastDeltaY());
                    WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());
                    wrapper.setOnGround(false);
                }

                checkNextPosition = false;
            }
        } else if (packet.isFlying()) {
            PositionProcessor pos = getData().getPositionProcessor();
            WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            if (!wrapper.isMoving() && wrapper.isOnGround() != pos.isOnGround() &&
                    /**!pos.isTeleporting() && **/wrapper.isOnGround()) {
                /**if (pos.getCurrentTeleportVec().equals(new Vector(pos.getX(), pos.getY(), pos.getZ()))) {
                    checkNextPosition = true;
                } else {**/
                    if (Math.abs(pos.getDeltaX()) < 0.03 || Math.abs(pos.getDeltaZ()) < 0.03) {
                        if (pos.getDeltaY() < -0.1553) {
                            data.getPlayer().sendMessage("debug BA - dy=" + pos.getDeltaY() + " ldy=" + pos.getLastDeltaY());
                            wrapper.setOnGround(false);
                        }
                    } else {
                        data.getPlayer().sendMessage("debug BA - dy=" + pos.getDeltaY() + " ldy=" + pos.getLastDeltaY());
                        wrapper.setOnGround(false);
                    }

            }
        }
    }
}