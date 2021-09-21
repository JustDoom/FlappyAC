package com.imjustdoom.flappyanticheat.checks.player.scaffold;

import com.cryptomorin.xseries.XMaterial;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(check = "Scaffold", checkType = "A", experimental = false, description = "Checks if the player placed a block in the air", type = CheckType.PLAYER)
public class ScaffoldA extends Check {

    public ScaffoldA(final FlappyPlayer player){
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        // Check if the packet is not a block place and if exempts are true, if true return
        if(!packet.isBlockPlace()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)
                || data.getActionProcessor().getLastBlockPlaced() == null || !isEnabled()) return;

        Block block = data.getActionProcessor().getLastBlockPlaced();
        boolean placedOnAir = true;

        List<Material> blockFace = new ArrayList<Material>() {{
            add(block.getRelative(BlockFace.UP).getType());
            add(block.getRelative(BlockFace.NORTH).getType());
            add(block.getRelative(BlockFace.EAST).getType());
            add(block.getRelative(BlockFace.SOUTH).getType());
            add(block.getRelative(BlockFace.WEST).getType());
            add(block.getRelative(BlockFace.DOWN).getType());
        }};

        // Loop through the block materials the block was placed on
        // and check if it was placed on air or a liquid
        for (Material material : blockFace) {
            if (material != XMaterial.AIR.parseMaterial() && material != XMaterial.LAVA.parseMaterial()
                    && material != XMaterial.WATER.parseMaterial() && material != XMaterial.CAVE_AIR.parseMaterial()
                    && material != XMaterial.VOID_AIR.parseMaterial()) {
                placedOnAir = false;
                break;
            }
        }

        if (placedOnAir) {
            fail("faces=" + blockFace, false);
        }
    }
}