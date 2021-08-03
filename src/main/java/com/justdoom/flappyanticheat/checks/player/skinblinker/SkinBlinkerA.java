package com.justdoom.flappyanticheat.checks.player.skinblinker;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "SkinBlinker", checkType = "A", experimental = false, description = "Skin settings being changed when crouching/sprinting")
public class SkinBlinkerA extends Check {

    public SkinBlinkerA(FlappyPlayer player) {
        super(player);
    }

    @Override
    public void handle(Packet packet){

        if(!packet.isSetting()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        int lastSkin = player.getSettingProcessor().getLastSkin();

        if (lastSkin == -1) return;

        if ((player.getPlayer().isSprinting()
                || player.getPlayer().isSneaking()
                || player.getPlayer().isBlocking())
                && lastSkin != player.getSettingProcessor().getSkin()) {
            fail("&7last=&2" + lastSkin + " &7current=&2" + player.getSettingProcessor().getSkin());
        }
    }
}