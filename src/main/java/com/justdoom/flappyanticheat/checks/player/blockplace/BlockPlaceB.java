package com.justdoom.flappyanticheat.checks.player.blockplace;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceB extends Check implements Listener {

    public BlockPlaceB() {
        super("BlockPlace", "B", true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();

        Block block = event.getBlock();

        ItemStack hand = player.getItemInHand();

        if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
            return;

        if(hand.getType() == Material.FLINT_AND_STEEL)
            return;

        if (block.getType() != hand.getType() && block.getType() != player.getInventory().getItemInOffHand().getType()) {

            Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("hand=" + hand.getType() + " placed=" + block.getType(), player));
        }
    }
}