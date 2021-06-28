package com.justdoom.flappyanticheat.checks.combat.range;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RangeA extends Check {

    private long lastKP;
    private int preVL;


    private List<BoundingBox> rayBB(PlayerData data, int ticks) {
        List<BoundingBox> toReturn = new ArrayList<>();
        if (data.pastVictimBoxes.size() > 4) {
            for (int range = 0; range < 2; range++) {
                toReturn.add(data.pastVictimBoxes.get(ticks + range));
            }
        }
        return toReturn;



    }

    public RangeA() {
        super("Range", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        PlayerData data = FlappyAnticheat.getInstance().dataManager.getData(event.getPlayer().getUniqueId());

        if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(event.getNMSPacket());
            if (packet.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                data.vicitm = (LivingEntity) packet.getEntity();
                if (data.pastVictimBoxes.size() != 0) {
                    int ticks = data.ping / 50;
                    List<BoundingBox> fromRay = this.rayBB(data,ticks);
                    Vector attacker = data.player.getEyeLocation().toVector();
                    double distance = fromRay.stream().mapToDouble(bb -> PlayerUtil.getDistanceBB(bb,attacker)).min().orElse(0);
                    if(distance > 3.2D) {
                        if(this.preVL++ > 4) {
                            fail("d=" + (float) distance, data.player);
                        }
                    }else this.preVL *= 0.975;
                    //Bukkit.broadcastMessage("distance=" + distance);


                }

            }

        } else if (event.getPacketId() == PacketType.Play.Client.KEEP_ALIVE) {
            data.ping = (int) (System.currentTimeMillis() - this.lastKP);
        }


    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (event.getPacketId() == PacketType.Play.Server.KEEP_ALIVE) {
            this.lastKP = System.currentTimeMillis();

        }
    }
}
