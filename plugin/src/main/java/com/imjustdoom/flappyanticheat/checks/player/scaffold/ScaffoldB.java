package com.imjustdoom.flappyanticheat.checks.player.scaffold;

import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.utils.player.Direction;

@CheckInfo(check = "Scaffold", checkType = "B", experimental = false, description = "Checks if the player is scaffolding downwards")
public class ScaffoldB extends Check {

    public ScaffoldB(final FlappyPlayer data){
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        if(!packet.isBlockPlace()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        final WrappedPacketInBlockPlace wrappedPacketInBlockPlace = new WrappedPacketInBlockPlace(packet.getRawPacket());

        // Check of the face the block was placed on was the bottom
        // and if the player is above where the block was placed
        if(wrappedPacketInBlockPlace.getDirection() == Direction.DOWN
                && data.getActionProcessor().getLastBlockPlaced().getY() < data.getPositionProcessor().getY())
            fail("No Info", false);
    }
}