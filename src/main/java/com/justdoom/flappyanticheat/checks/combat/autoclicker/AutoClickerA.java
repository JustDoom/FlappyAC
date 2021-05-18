
package com.justdoom.flappyanticheat.checks.combat.killaura;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

/**
 *  ported by pixchure
 *  
 */

public class AutoClickerA extends Check {

	private int swings;
	private int movements;

	public AutoClickerA(PlayerData playerData) {
		  super("AutoClicker", "A", true);
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		//double vl = playerData.getCheckVl(this);

		if (packet instanceof PacketPlayInArmAnimation
				&& !playerData.isDigging() && !playerData.isPlacing() && !playerData.isFakeDigging()
				&& (System.currentTimeMillis() - playerData.getLastDelayedMovePacket()) > 220L
				&& (System.currentTimeMillis() - playerData.getLastMovePacket().getTimestamp()) < 110L) {
			++swings;
		} else if (packet instanceof PacketPlayInFlying && ++movements == 20) {
			if (swings > 35) {
				fail("", player);
			}
			movements = swings = 0;
		}
	}

}
