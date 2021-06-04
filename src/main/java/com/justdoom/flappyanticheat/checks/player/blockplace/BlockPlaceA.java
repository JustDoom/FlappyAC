package com.justdoom.flappyanticheat.checks.player.blockplace;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockPlaceA extends Check implements Listener {

    public BlockPlaceA() {
        super("BlockPlace", "A", true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
            return;

        Player player = event.getPlayer();

        Block block = event.getBlock();
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
            Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("faces=" + blockFace, player));
        }
    }
}
