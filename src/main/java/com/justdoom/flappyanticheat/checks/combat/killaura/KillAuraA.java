package com.justdoom.flappyanticheat.checks.combat.killaura;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

/**
 *  ported by Pixchure 
 *   not even sure if this works
 */

public class KillAuraA extends Check {
    private boolean sent;
    public KillAuraA(){
        super("KillAura", "A", false);
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        //double vl = playerData.getCheckVl(this);

        if (playerData.getPing() > 70) {
            return;
        }

        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity
                .EnumEntityUseAction.ATTACK) {
            if (!sent) {
                fail()
            } else {
                sent = false;
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            sent = true;
        } else if (packet instanceof PacketPlayInFlying) {
            sent = false;
        }
        //playerData.setCheckVl(vl, this);
    }
}
