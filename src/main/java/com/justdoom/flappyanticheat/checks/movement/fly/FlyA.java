package com.justdoom.flappyanticheat.checks.movement.fly;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.PlayerUtil;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CheckInfo(check = "Fly", checkType = "A", experimental = false, description = "Fly")
public class FlyA extends Check {

    private double stableY;

    public FlyA(FlappyPlayer player) {
        super(player);
    }

    public void handle(final Packet packet) {

        if (!packet.isPosition() || isExempt(ExemptType.TPS, ExemptType.GAMEMODE)) return;

        if (player.getPositionProcessor().getY() == player.getPositionProcessor().getLastY() && player.getPositionProcessor().isInAir()) {
            this.stableY++;
        } else {
            this.stableY = 0.0D;
        }
        if (this.stableY > 2.0D)
            fail("No Info");
    }
}