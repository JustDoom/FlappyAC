package com.imjustdoom.flappyanticheat.checks.player.skinblinker;

import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "SkinBlinker", checkType = "A", experimental = false, description = "Skin settings being changed when crouching/sprinting", type = CheckType.PLAYER)
public class SkinBlinkerA extends Check {

    public SkinBlinkerA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet){

        // Check the packet type and exempts
        if(!packet.isSetting()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        // Get last skin
        int lastSkin = data.getSettingProcessor().getLastSkin();

        if (lastSkin == -1) return;

        // Check if player is not able to change their skin settings and
        // if the skin is different to the last skin
        if ((data.getPlayer().isSprinting()
                || data.getPlayer().isSneaking()
                || data.getPlayer().isBlocking())
                && lastSkin != data.getSettingProcessor().getSkin())
            fail("&7last=&2" + lastSkin + " &7current=&2" + data.getSettingProcessor().getSkin(), false);
    }
}