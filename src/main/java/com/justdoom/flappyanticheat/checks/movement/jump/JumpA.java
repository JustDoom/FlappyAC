package com.justdoom.flappyanticheat.checks.movement.jump;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JumpA extends Check {

    private Map<UUID, Double> sinceSlimeTicks = new HashMap<>();

    private Map<UUID, Boolean> blockAbove = new HashMap<>();
    private Map<UUID, Boolean> colliding = new HashMap<>();
    private Map<UUID, Boolean> onSlime = new HashMap<>();

    public JumpA(){
        super("Jump", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        double sinceSlimeTicks = this.sinceSlimeTicks.getOrDefault(uuid, 0.0);

        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());

            if (player.isFlying()) return;

            //about 0.42 is actual jump height, but account for some desync
            //had to skid the potion level util from medusa, sorry
            double jumpSize = 0.43f + (double) + ((float) PlayerUtil.getPotionLevel(player, PotionEffectType.JUMP) * 0.1f);

            double lastY = player.getLocation().getY();
            final double deltaY = packet.getPosition().getY() - lastY;
            final boolean onGround = packet.isOnGround();

            //we use this to check if they actually jumped. if their y changed and if their previous y divide by 1/64th
            //had a remainder of 0
            boolean jumped = deltaY > 0 && lastY % (1D/64) == 0;

            //this is probably really intensive, but theres no current better method

            //check if the blocks above the player are air
            for (Block block : PlayerUtil.getNearbyBlocksHorizontally(new Location(player.getWorld(), player.getLocation().getX(),
                    player.getLocation().getY() + 2, player.getLocation().getZ()), 1)) {
                if (block.getType() != Material.AIR) {
                    this.blockAbove.put(uuid, true);
                    break;
                } else {
                    this.blockAbove.put(uuid, false);
                }
            }

            //check if anythings colliding inside the player
            for (Block block : PlayerUtil.getNearbyBlocks(new Location(player.getWorld(), player.getLocation().getX(),
                    player.getLocation().getY() + 1, player.getLocation().getZ()), 1)) {
                if (!block.isPassable()) {
                    this.colliding.put(uuid, true);
                    break;
                } else {
                    this.colliding.put(uuid, false);
                }
            }

            //check if the player is on a slime block
            for (Block block : PlayerUtil.getNearbyBlocksHorizontally(new Location(player.getWorld(), player.getLocation().getX(),
                    player.getLocation().getY() -1, player.getLocation().getZ()), 1)) {
                if (block.getType() == Material.SLIME_BLOCK) {
                    this.onSlime.put(uuid, true);
                    break;
                } else {
                    this.onSlime.put(uuid, false);
                }
            }

            //grab our booleans from the hashmaps
            boolean blockAbove = this.blockAbove.getOrDefault(uuid, false);
            boolean colliding = this.colliding.getOrDefault(uuid, false);
            boolean onSlime = this.onSlime.getOrDefault(uuid, false);

            //if the player isnt colliding with anything, isnt on slime, or doesnt have a block above then they can flag.
            boolean canFlag = !colliding && !onSlime && !blockAbove;

            //flag the player if theyre under the normal jump size, jumped, and are eligible for flag.
            if (deltaY < (jumpSize - 0.02) && jumped && canFlag) {
                fail("",player);
            }

            //flag the player if they go over our jump size limit and havent been on slime blocks for 15 ticks
            if (deltaY > (onGround ? 0.6 : jumpSize) && sinceSlimeTicks >= 15) {
                fail("", player);
            }
        }

        boolean onSlime = this.onSlime.getOrDefault(uuid, false);

        sinceSlimeTicks = onSlime ? 0 : sinceSlimeTicks + 1;

        this.sinceSlimeTicks.put(uuid, sinceSlimeTicks);
    }
}