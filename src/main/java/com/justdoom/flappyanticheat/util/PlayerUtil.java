package com.justdoom.flappyanticheat.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public final class PlayerUtil {

    public static int getPing(final Player player) {
        try {
            final Method handle = player.getClass().getMethod("getHandle");
            final Object nmsHandle = handle.invoke(player);
            final Field pingField = nmsHandle.getClass().getField("ping");
            return pingField.getInt(nmsHandle);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
                | NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while trying to get player ping.", e);
        }

        return -1;
    }

    public static boolean isOnClimbable(final Player player) {
        return player.getLocation().getBlock().getType() == Material.LADDER || player.getLocation().getBlock().getType() == Material.VINE;
    }

    public static Set<Block> getNearbyBlocksHorizontally(final Location location, final int radius) {
        final Set<Block> blocks = new HashSet<>();

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY(); y <= location.getBlockY(); y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    //im sorry for this, but this is the best botch i have LMFAO
    //since i cant use the regular method and not expand vertical radius, just remove the radius in a new util ;)
    // - Spiriten

    public static Set<Block> getNearbyBlocks(Location location, final int radius) {
        final Set<Block> blocks = new HashSet<>();

        location = location.clone();

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static boolean isFlying(final Player player) {
        return player.isFlying();
    }

    public static double getDistanceBB(final BoundingBox origin, final Vector attacker) {
        final double x = Math.min(Math.pow(attacker.getX() - origin.getMinX(), 2.0), Math.pow(attacker.getX() - origin.getMaxX(), 2.0));
        final double z = Math.min(Math.pow(attacker.getZ() - origin.getMinZ(), 2.0), Math.pow(attacker.getZ() - origin.getMaxZ(), 2.0));
        return Math.sqrt(x + z);
    }

    //From AntiHaxerMan
    public static List<Entity> getEntitiesWithinRadius(final Location location, final double radius) {
        try {
            final double expander = 16.0D;

            final double x = location.getX();
            final double z = location.getZ();

            final int minX = (int) Math.floor((x - radius) / expander);
            final int maxX = (int) Math.floor((x + radius) / expander);

            final int minZ = (int) Math.floor((z - radius) / expander);
            final int maxZ = (int) Math.floor((z + radius) / expander);

            final World world = location.getWorld();

            final List<Entity> entities = new LinkedList<>();

            for (int xVal = minX; xVal <= maxX; xVal++) {

                for (int zVal = minZ; zVal <= maxZ; zVal++) {

                    if (!world.isChunkLoaded(xVal, zVal)) continue;

                    for (final Entity entity : world.getChunkAt(xVal, zVal).getEntities()) {
                        //We have to do this due to stupidness
                        if (entity == null) break;

                        //Make sure the entity is within the radius specified
                        if (entity.getLocation().distanceSquared(location) > radius * radius) continue;

                        entities.add(entity);
                    }
                }
            }

            return entities;
        } catch (final Throwable t) {
            // I know stfu
        }

        return null;
    }

    public boolean isInLiquid(final Player player) {
        final double expand = 0.31;
        final Location location = player.getLocation();
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (player.getWorld().getBlockAt(location.clone().add(x, -0.5001, z)).isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getPotionLevel(final Player player, final PotionEffectType effect) {
        final int effectId = effect.getId();

        if (!player.hasPotionEffect(effect)) return 0;

        for (final PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getId() == effectId) {
                return potionEffect.getAmplifier() + 1;
            }
        }

        return 0;
    }
}