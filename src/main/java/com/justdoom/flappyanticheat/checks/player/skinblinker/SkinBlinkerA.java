package com.justdoom.flappyanticheat.checks.player.skinblinker;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.setcreativeslot.WrappedPacketInSetCreativeSlot;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;
import org.bukkit.entity.Player;

public class SkinBlinkerA extends Check {

    public SkinBlinkerA(){
        super("SkinBlinker", "A", false);
    }

    private int lastSkin = -1;

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if (event.getPacketId() == PacketType.Play.Client.SETTINGS){
            WrappedPacketInSettings packet = new WrappedPacketInSettings(event.getNMSPacket());
            Player player = event.getPlayer();

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            if (lastSkin == -1) {
                lastSkin = packet.getDisplaySkinPartsMask();
                return;
            }

            if ((player.isSprinting()
                    || player.isSneaking()
                    || player.isBlocking())
                    && lastSkin != packet.getDisplaySkinPartsMask()) {
                fail("&7last=&2" + lastSkin + " &7current=&2" + packet.getDisplaySkinPartsMask(), player);
            }

            lastSkin = packet.getDisplaySkinPartsMask();
        }
    }
}