package com.justdoom.flappyanticheat.checks.movement.fly;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
import com.justdoom.flappyanticheat.utils.PlayerUtil;
import com.justdoom.flappyanticheat.utils.ServerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyA extends Check {

    private double buffer;
    private double lastResult, lastDeltaY;
    private boolean lastLastOnGround, lastOnGround;
    private Location lastLocation;

    public FlyA(){
        super("Fly", "A", false);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            if(ServerUtil.lowTPS(("checks." + check + "." + checkType).toLowerCase()))
                return;

            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());
            Player player = event.getPlayer();

            final double deltaY = packet.getY() - player.getLocation().getY();

            final double lastDeltaY = this.lastDeltaY;
            this.lastDeltaY = deltaY;

            final boolean onGround = packet.isOnGround();

            final double prediction = Math.abs((lastDeltaY - 0.08) * 0.98F) < 0.005 ? -0.08 * 0.98F : (lastDeltaY - 0.08) * 0.98F;
            final double difference = Math.abs(deltaY - prediction);

            final boolean invalid = difference > 0.079D
                    && !onGround
                    && !(packet.getY() % 0.5 == 0 && packet.isOnGround() && lastDeltaY < 0);

            if (invalid) {
                buffer += buffer < 50 ? 10 : 0;
                if (buffer > 20) {
                    fail("diff=%.4f, buffer=%.2f, at=%o", player);
                }
            } else {
                buffer = Math.max(buffer - 0.75, 0);
            }
        }
    }
}