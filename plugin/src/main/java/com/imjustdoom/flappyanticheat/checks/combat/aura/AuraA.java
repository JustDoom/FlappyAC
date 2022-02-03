package com.imjustdoom.flappyanticheat.checks.combat.aura;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.imjustdoom.api.check.CheckInfo;
import com.imjustdoom.api.check.CheckType;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.packet.Packet;

@CheckInfo(check = "Aura", checkType = "A", experimental = true, description = "check for correct order between packets", type = CheckType.COMBAT)
public class AuraA extends Check {

    private long lastSent;
    private double verbose;

    public AuraA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {

        if (packet.isFlying()) {
            this.lastSent = System.currentTimeMillis();
        } else if (packet.isUseEntity()) {

            final WrapperPlayClientInteractEntity wrappedUse = new WrapperPlayClientInteractEntity(packet.getEvent());

            if (wrappedUse.getAction().equals(WrapperPlayClientInteractEntity.InteractAction.ATTACK)) {

                final long difference = Math.abs(System.currentTimeMillis() - lastSent);

                if (difference < 25L) {
                    if (++verbose > 6)
                        fail("diff=" + difference, false);
                } else if(verbose > 0) verbose -= 0.5D;
            }
        }
    }

}
