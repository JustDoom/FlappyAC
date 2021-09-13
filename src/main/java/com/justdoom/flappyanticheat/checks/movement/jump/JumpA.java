package com.justdoom.flappyanticheat.checks.movement.jump;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
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
    private Map<UUID, Boolean> onInvalid = new HashMap<>();
    private Map<UUID, Boolean> stairsDeserveTheDeathSentence = new HashMap<>();

    public JumpA(){
        super("Jump", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if(PacketEvents.get().getPlayerUtils().isGeyserPlayer(event.getPlayer().getPlayer())) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        double sinceSlimeTicks = this.sinceSlimeTicks.getOrDefault(uuid, 0.0);

        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());

            if (player.isFlying() || player.isDead() || player.isInsideVehicle() || player.isGliding()) return;

            //0.42 is not the real jump height of the player. if youre gonna make it false atleast use the right number.
            //vehicle desync may false this, thats why it was originally 0.43. it seems like izibane likes false flags
            //though, so ill go along with it
            //0.41999998688697815 is our exact value.
            double jumpSize = 0.41999998688697815f + (double) + ((float) PlayerUtil.getPotionLevel(player, PotionEffectType.JUMP) * 0.11f);

            double lastY = player.getLocation().getY();
            final double deltaY = packet.getPosition().getY() - lastY;
            final boolean onGround = packet.isOnGround();

            //we use this to check if they actually jumped. if their y changed and if their previous y divided by 1/64th
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

            //check if the player is on a slime block or an unsupported block
            for (Block block : PlayerUtil.getNearbyBlocksHorizontally(new Location(player.getWorld(), player.getLocation().getX(),
                    player.getLocation().getY() - 1, player.getLocation().getZ()), 2)) {
                if ((block.getType().toString().contains("SLIME"))) {
                    this.onSlime.put(uuid, true);
                    break;
                } else {
                    this.onSlime.put(uuid, false);
                }

                if ((block.getType().toString().contains("STAIR")) || (block.getType().toString().contains("SLAB")) ||
                        (block.getType().toString().contains("PISTON")) || (block.getType().toString().contains("FENCE")) ||
                        (block.getType().toString().contains("WALL"))) {
                    this.onInvalid.put(uuid, true);
                    break;
                } else {
                    this.onInvalid.put(uuid, false);
                }
            }

            //stairs should be removed from the game. all they cause is pain and suffering and pain and hurt and help
            for (Block block : PlayerUtil.getNearbyBlocksHorizontally(new Location(player.getWorld(), player.getLocation().getX(),
                    player.getLocation().getY(), player.getLocation().getZ()), 2)) {
                if ((block.getType().toString().contains("STAIR")) || (block.getType().toString().contains("SLAB"))) {
                    this.stairsDeserveTheDeathSentence.put(uuid, true);
                    break;
                } else {
                    this.stairsDeserveTheDeathSentence.put(uuid, false);
                }
            }

            //grab our booleans from the hashmaps
            boolean blockAbove = this.blockAbove.getOrDefault(uuid, false);
            boolean colliding = this.colliding.getOrDefault(uuid, false);
            boolean onSlime = this.onSlime.getOrDefault(uuid, false);
            boolean onInvalid = this.onInvalid.getOrDefault(uuid, false);
            boolean deathSentence = this.stairsDeserveTheDeathSentence.getOrDefault(uuid, false);

            //if the player isnt colliding with anything, isnt on slime, or doesnt have a block above then they can flag.
            boolean canFlag = !colliding && !onSlime && !blockAbove && !onInvalid;

            //flag the player if theyre under the normal jump size, jumped, and are eligible for flag.
            if (deltaY < (jumpSize - 0.02) && jumped && canFlag) {
                fail("low jump" + deltaY,player);
            }

            //so we check if theyre packet onground first. if they are, then we use 0.6 as their jump height. its not
            //worth going for the exact number. if theyre off the ground, their max jump size is outlines by our jumpsize
            //double. we also want to make sure theyve been off of slime for atleast 15 ticks
            if (deltaY > (onGround ? 0.6 : jumpSize) && sinceSlimeTicks >= 15 && !onInvalid && !deathSentence) {
                fail("high jump" + deltaY , player);
            }
        }

        boolean onSlime = this.onSlime.getOrDefault(uuid, false);

        sinceSlimeTicks = onSlime ? 0 : sinceSlimeTicks + 1;

        this.sinceSlimeTicks.put(uuid, sinceSlimeTicks);
    }
}
