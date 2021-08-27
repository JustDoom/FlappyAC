package com.justdoom.flappyanticheat.checks.player.scaffold;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Scaffold", checkType = "B", experimental = false, description = "Checks if the ???")
public class ScaffoldB extends Check {

    public ScaffoldB(final FlappyPlayer data){
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isBlockPlace()) return;

        //data.getPlayer().sendMessage(data.getActionProcessor().getBlockPlacedAgainst().getType().name());
    }
}
