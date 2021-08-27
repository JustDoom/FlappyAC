package com.justdoom.flappyanticheat.checks.player.scaffold;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

@CheckInfo(check = "Scaffold", checkType = "A", experimental = false, description = "Checks if the player placed a block in the air")
public class ScaffoldA extends Check {

    public ScaffoldA(final FlappyPlayer player){
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        if(!packet.isBlockPlace()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)
                || data.getActionProcessor().getLastBlockPlaced() == null) return;

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

        for (Material material : blockFace) {
            if (material != Material.AIR && material != Material.LAVA && material != Material.WATER && material != Material.CAVE_AIR && material != Material.VOID_AIR) {
                placedOnAir = false;
                break;
            }
        }
        if (placedOnAir) {
            fail("faces=" + blockFace);
        }
    }
}