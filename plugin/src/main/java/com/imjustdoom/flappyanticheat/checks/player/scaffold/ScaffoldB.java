package com.imjustdoom.flappyanticheat.checks.player.scaffold;

import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Scaffold", checkType = "B", experimental = false, description = "Checks if the player is scaffolding downwards", type = CheckType.PLAYER)
public class ScaffoldB extends Check {

    public ScaffoldB(final FlappyPlayer data){
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        // Check if the packet is not a block place and if exempts are true, if true return
        if(!packet.isBlockPlace()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        final WrapperPlayClientPlayerBlockPlacement wrappedPacketInBlockPlace = new WrapperPlayClientPlayerBlockPlacement(packet.getEvent());

        // Check of the face the block was placed on was the bottom
        // and if the player is above where the block was placed
        if(wrappedPacketInBlockPlace.getFace() == BlockFace.DOWN
                && data.getActionProcessor().getLastBlockPlaced().getY() < data.getPositionProcessor().getY())
            fail("No Info", false);
    }
}