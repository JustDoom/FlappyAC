package com.imjustdoom.flappyanticheat.checks.movement.speed;

import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.exempt.type.ExemptType;
import com.imjustdoom.flappyanticheat.packet.Packet;
import com.imjustdoom.flappyanticheat.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(check = "Speed", checkType = "C", experimental = false, description = "Checks players speed on the ground", type = CheckType.MOVEMENT)
public class SpeedC extends Check {

    public SpeedC(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        if (!packet.isPosition() && !packet.isPositionLook()
                || data.getPositionProcessor().getY() == data.getPositionProcessor().getLastY()
                || isExempt(ExemptType.JOINED, ExemptType.ENTITIES, ExemptType.FLYING/**ExemptType.WEB, ExemptType.TELEPORT**/)) return;


    }
}