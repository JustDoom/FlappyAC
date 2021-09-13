package com.imjustdoom.flappyanticheat.checks.movement.nofall;

import com.cryptomorin.xseries.XMaterial;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(check = "NoFall", checkType = "B", experimental = false, description = "Checks if the players fall distance is smaller than the last fall distance")
public class NoFallB extends Check {

    public NoFallB(FlappyPlayer player) {
        super(player);
    }

    private double lastFallDistance;

    @Override
    public void handle(Packet packet) {

        if(!packet.isPosition()
                && !packet.isLook()
                && !packet.isPositionLook()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS, ExemptType.JOINED,
                ExemptType.PISTON, ExemptType.SHULKER, ExemptType.VEHICLE)) return;

        int airTicks = data.getPositionProcessor().getAirTicks();
        double fallDistance = data.getPlayer().getFallDistance();


        List<Material> blocks = new ArrayList<>();
        final Location loc = new Location(data.getPlayer().getWorld(), data.getPositionProcessor().getX(),
                data.getPositionProcessor().getY() - 2, data.getPositionProcessor().getZ());

        for(int x = loc.getBlockX(); x <= loc.getBlockX(); x++) {
            for(int z = loc.getBlockZ(); z <= loc.getBlockZ(); z++) {
                blocks.add(loc.getWorld().getBlockAt(x, loc.getBlockY(), z).getType());
                data.getPlayer().sendMessage(String.valueOf(loc.getWorld().getBlockAt(x, loc.getBlockY(), z).getType()));
            }
        }

        if(fallDistance < lastFallDistance && airTicks > 10 && !blocks.contains(XMaterial.AIR.parseMaterial())){
            fail("fallDistance: " + fallDistance + " lastFallDistance: " + lastFallDistance
                    + " blocks: " + !blocks.contains(XMaterial.AIR.parseMaterial()), false);
        }

        lastFallDistance = fallDistance;
    }
}