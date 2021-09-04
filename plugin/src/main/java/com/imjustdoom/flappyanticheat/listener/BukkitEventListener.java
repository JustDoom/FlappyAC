package com.imjustdoom.flappyanticheat.listener;

import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BukkitEventListener implements Listener {

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final FlappyPlayer player = FlappyAnticheat.INSTANCE.getDataManager().getData(event.getPlayer());

        // Handle the event
        if (player != null) player.getActionProcessor().handleBlockPlace(event.getBlockPlaced(),
                event.getBlockAgainst());
    }
}