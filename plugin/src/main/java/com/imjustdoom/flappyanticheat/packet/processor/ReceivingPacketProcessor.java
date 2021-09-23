package com.imjustdoom.flappyanticheat.packet.processor;

import com.imjustdoom.api.check.FlappyCheck;
import com.imjustdoom.flappyanticheat.checks.Check;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import com.imjustdoom.flappyanticheat.data.processor.PositionProcessor;
import com.imjustdoom.flappyanticheat.packet.Packet;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import net.minestom.server.network.packet.client.play.ClientSettingsPacket;
import net.minestom.server.network.packet.client.play.ClientVehicleMovePacket;

public class ReceivingPacketProcessor {

    /**
     * Handles incoming packets
     *
     * @param player - Player to handle the packets for
     * @param packet - The packet to handle
     */
    public void handle(final FlappyPlayer player, Packet packet) {

        if (packet.isLook() || packet.isPositionLook()) {
            final ClientPlayerRotationPacket wrapper = (ClientPlayerRotationPacket) packet.getRawPacket();
            player.getRotationProcessor().handle(wrapper.yaw, wrapper.pitch);
        }

        if (packet.isPosition() || packet.isPositionLook()) {
            final ClientPlayerPositionPacket wrapper = (ClientPlayerPositionPacket) packet.getRawPacket();
            PositionProcessor pos = player.getPositionProcessor();
            if (wrapper.x != pos.getX() || wrapper.y != pos.getY() || wrapper.z != pos.getZ())
                player.getPositionProcessor().handle(wrapper.x, wrapper.y, wrapper.z, wrapper.onGround);
        }

        if (packet.isVehicleMove()) {
            final ClientVehicleMovePacket wrapper = (ClientVehicleMovePacket) packet.getRawPacket();

            player.getPositionProcessor().handle(wrapper.x, wrapper.y, wrapper.z, false);
        }

        if (packet.isSetting()) {
            final ClientSettingsPacket wrapper = (ClientSettingsPacket) packet.getRawPacket();

            player.getSettingProcessor().handle(wrapper);
        }

        // TODO: transaction packet
        if (packet.isIncomingTransaction()) {
            //final WrappedPacketInTransaction wrapper = new WrappedPacketInTransaction(packet.getRawPacket());
            //player.getVelocityProcessor().handleTransaction(wrapper);
        }

        if (player.getPlayer().hasPermission("flappyac.bypass")) return;

        for (final FlappyCheck check : player.getChecks()) {
            ((Check) check).handle(packet);
        }
    }
}