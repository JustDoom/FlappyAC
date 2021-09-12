package com.imjustdoom.flappyanticheat.checks.player.inventory;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import org.bukkit.entity.Player;

@CheckInfo(check = "Inventory", checkType = "A", experimental = false, description = "Sprinting/Crouching while clicking in an inventory")
public class InventoryA extends Check {

    public InventoryA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(!packet.isInventoryClick()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS)) return;

        final Player player = data.getPlayer();
        if(player.isSprinting() || player.isSneaking()) {
            fail("No Info", false);
        }
    }
}