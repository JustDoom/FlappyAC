package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;

@CheckInfo(check = "Speed", checkType = "A", experimental = false, description = "Fly")
public class SpeedA extends Check{

    public SpeedA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        if (packet.isPosition()) {

            
        }
    }
}