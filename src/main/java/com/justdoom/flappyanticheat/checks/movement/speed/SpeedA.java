package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedA extends Check implements Listener {

    public SpeedA() {
        super("Speed", "A", true);
    }

    @EventHandler
    public void onPacketPlayReceive(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData data = FlappyAnticheat.getInstance().dataManager.getData(event.getPlayer().getUniqueId());

        //dont run the check if they have /fly on or are creative flying
        if (player.isFlying() || player.isGliding() || player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) return;
        if(PacketEvents.get().getPlayerUtils().isGeyserPlayer(event.getPlayer().getPlayer())) return;

        //pos stuff
        data.lastOnGround = data.onGround;
        data.onGround = player.isOnGround();

        data.lastDeltaXZ = data.deltaXZ;

        double deltaX, deltaZ;
        deltaX = event.getTo().getX() - event.getFrom().getX();
        deltaZ = event.getTo().getZ() - event.getFrom().getZ();
        data.deltaXZ = Math.hypot(deltaX, deltaZ);

        //velocity stuff
        double velocityX, velocityZ;
        velocityX = player.getVelocity().getX();
        velocityZ = player.getVelocity().getZ();
        data.velocityXZ = Math.hypot(velocityX, velocityZ);

        data.entities = player.getNearbyEntities(1.5, 10, 1.5);

        if (ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
            return;

        if(player.isGliding() || (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.ELYTRA)) return;

        //Thanks to sprit for this check - modified it to fit with the base, its usually much better :/
        if (!data.onGround && !data.lastOnGround && !(data.entities.size() > 0)) {
            double prediction = data.lastDeltaXZ * 0.91F + 0.0259F;
            double accuracy = (data.deltaXZ - prediction);

            if (accuracy > 0.001 && data.deltaXZ > 0.1) {
                if (Math.abs(data.velocityXZ - data.deltaXZ) > 0.04) {
                    Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("exp=" + prediction + " got=" + data.deltaXZ + " vel=" +
                            data.velocityXZ, player));
                } else {
                    Bukkit.getScheduler().runTaskAsynchronously(FlappyAnticheat.getInstance(), () -> fail("exp=" + prediction + " got=" + data.deltaXZ, player));
                }
            }
        }
    }
}