package com.imjustdoom.flappyanticheat.checks.player.inventory;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import net.minestom.server.entity.Player;

@CheckInfo(check = "Inventory", checkType = "A", experimental = false, description = "Sprinting/Crouching while clicking in an inventory", type = CheckType.PLAYER)
public class InventoryA extends Check {

    public InventoryA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        // Check if the packet is not an inventory click and if exempts are true, if true return
        if(!packet.isInventoryClick()
                || isExempt(ExemptType.GAMEMODE, ExemptType.TPS) || !isEnabled()) return;

        // Check of the player is sprinting or sneaking
        final Player player = data.getPlayer();
        if(player.isSprinting() || player.isSneaking()) {
            fail("No Info", false);
        }
    }
}