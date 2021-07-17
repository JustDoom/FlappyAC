package com.justdoom.flappyanticheat.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class PlayerUtil {

    public static int getPing(Player player) {
        /**try {
            Method handle = player.getClass().getMethod("getHandle");
            Object nmsHandle = handle.invoke(player);
            Field pingField = nmsHandle.getClass().getField("ping");
            return pingField.getInt(nmsHandle);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
                | NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while trying to get player ping.", e);
        }**/

        return -1;
    }

    public static boolean isInLiquid(Player player) {
        if(player.isInWater() || player.getLocation().getBlock().getType() == Material.LAVA){
            return true;
        }
        return false;
    }

    public static boolean isOnClimbable(Player player) {
        if(player.getLocation().getBlock().getType() == Material.LADDER || player.getLocation().getBlock().getType() == Material.VINE ){
            return true;
        }
        return false;
    }

    //im sorry for this, but this is the best botch i have LMFAO
    //since i cant use the regular method and not expand vertical radius, just remove the radius in a new util ;)
    // - Spiriten

    public static Set<Block> getNearbyBlocksHorizontally(Location location, int radius) {
        Set<Block> blocks = new HashSet<>();

        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY(); y <= location.getBlockY(); y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static Set<Block> getNearbyBlocks(Location location, int radius) {
        Set<Block> blocks = new HashSet<>();

        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static boolean isFlying(Player player){
        return player.isFlying();
    }

    public static double getDistanceBB(BoundingBox origin, Vector attacker) {
        double x = Math.min(Math.pow(attacker.getX() - origin.getMinX(),2.0), Math.pow(attacker.getX() - origin.getMaxX(),2.0));
        double z = Math.min(Math.pow(attacker.getZ() - origin.getMinZ(),2.0), Math.pow(attacker.getZ() - origin.getMaxZ(),2.0));
        return Math.sqrt(x + z);
    }

    public static int getPotionLevel(Player player, PotionEffectType effect) {
        final int effectId = effect.getId();

        if (!player.hasPotionEffect(effect)) return 0;

        return player.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().getId() == effectId).map(PotionEffect::getAmplifier).findAny().orElse(0) + 1;
    }

    public static Set<Block> getNearbyBlocksConfigurable(Location location, int radiusX, int radiusY, int radiusZ) {
        Set<Block> blocks = new HashSet<>();

        for(int x = location.getBlockX() - radiusX; x <= location.getBlockX() + radiusX; x++) {
            for(int y = location.getBlockY() - radiusY; y <= location.getBlockY() + radiusY; y++) {
                for(int z = location.getBlockZ() - radiusZ; z <= location.getBlockZ() + radiusZ; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }
}
