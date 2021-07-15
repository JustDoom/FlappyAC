package com.justdoom.flappyanticheat.checks.movement.groundspoof;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@CheckInfo(check = "GroundSpoof", checkType = "A", experimental = false)
public class GroundSpoofA extends Check {

    private int buffer = 0;

    public GroundSpoofA(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet) {

        //if(event.getPacketId() != PacketType.Play.Client.POSITION || event.getPacketId() != PacketType.Play.Client.LOOK || event.getPacketId() != PacketType.Play.Client.POSITION_LOOK)
            //return;

        //FlappyPlayer player = FlappyAnticheat.getInstance().dataManager.getData(packet);

        double groundY = 0.015625;
        boolean client = player.getPositionProcessor().isOnGround(), server = player.getPositionProcessor().getY() % groundY < 0.0001;

        if (client != server) { //  && !player.isOnClimbable()
            if (++buffer > 1) {

                boolean boat = false;
                boolean shulker = false;
                boolean pistonHead = false;

                AtomicReference<List<Entity>> nearby = new AtomicReference<>();
                //sync(() -> nearby.set(player.getNearbyEntities(1.5, 10, 1.5)));

                /**for (Entity entity : nearby.get()) {
                    if (entity.getType() == EntityType.BOAT && player.getPositionProcessor().getY() > entity.getLocation().getY()) {
                        boat = true;
                        break;
                    }

                    if (entity.getType() == EntityType.SHULKER && player.getPositionProcessor().getY() > entity.getBoundingBox().getMinY()) {
                        shulker = true;
                        break;
                    }
                }**/

                for (Block block : PlayerUtil.getNearbyBlocks(new Location(player.getPlayer().getWorld(), player.getPositionProcessor().getX(), player.getPositionProcessor().getY(), player.getPositionProcessor().getZ()), 2)) {

                    if (Tag.SHULKER_BOXES.isTagged(block.getType())) {
                        shulker = true;
                        break;
                    }

                    if (block.getType() == Material.PISTON_HEAD) {
                        pistonHead = true;
                        break;
                    }
                }

                if (!boat && !shulker && !pistonHead) {
                    fail();
                }
            }
        } else if (buffer > 0) buffer--;
    }
}

/**
 * https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/check/impl/movement/speed/SpeedA.java
 * https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/check/Check.java
 * https://github.com/GladUrBad/Medusa/blob/master/Impl/src/main/java/com/gladurbad/medusa/packet/Packet.java
 * https://github.com/GladUrBad/Medusa/blob/master/Impl/src/main/java/com/gladurbad/medusa/packet/processor/ReceivingPacketProcessor.java
 * https://github.com/GladUrBad/Medusa/blob/master/Impl/src/main/java/com/gladurbad/medusa/data/processor/PositionProcessor.java
 * https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/manager/CheckManager.java#L112
 * https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/data/PlayerData.java
 */