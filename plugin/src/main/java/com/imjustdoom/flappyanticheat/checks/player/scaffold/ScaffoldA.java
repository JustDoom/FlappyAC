package com.imjustdoom.flappyanticheat.checks.player.scaffold;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.Material;

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
                || data.getActionProcessor().getBlockPlaced() == null || !isEnabled()) return;

        Block block = data.getActionProcessor().getBlockPlaced();
        boolean placedOnAir = true;

        List<Material> blockFace = new ArrayList<Material>() /**{{
            add(block.getRelative(BlockFace.TOP).getType());
            add(block.getRelative(BlockFace.NORTH).getType());
            add(block.getRelative(BlockFace.EAST).getType());
            add(block.getRelative(BlockFace.SOUTH).getType());
            add(block.getRelative(BlockFace.WEST).getType());
            add(block.getRelative(BlockFace.BOTTOM).getType());
        }}**/;

        // Loop through the block materials the block was placed on
        // and check if it was placed on air or a liquid
        for (Material material : blockFace) {
            if (material != Material.AIR && material != Block.LAVA.registry().material()
                    && material != Block.WATER.registry().material()) {
                placedOnAir = false;
                break;
            }
        }

        if (placedOnAir) {
            fail("faces=" + blockFace, false);
        }
    }
}