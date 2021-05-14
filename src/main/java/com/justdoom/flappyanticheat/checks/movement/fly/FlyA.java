package com.justdoom.flappyanticheat.checks.movement.fly;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@CheckData(name = "Fly", type = "A")
public class FlyA extends Check {

    private float buffer;
    private double lastResult, lastDeltaY;
    private boolean lastLastOnGround, lastOnGround;
    private Location lastLocation = data.player.getLocation();

    public FlyA(PlayerData data) {
        super(data);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {

            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());
            Player player = event.getPlayer();

            Location currentLoc = new Location(player.getWorld(), packet.getX(), packet.getY(), packet.getZ());
            Location lastLocation = this.lastLocation;
            this.lastLocation = currentLoc;

            boolean onGround = packet.isOnGround();
            boolean lastOnGround = this.lastOnGround;
            this.lastOnGround = onGround;
            boolean lastLastOnGround = this.lastLastOnGround;
            this.lastLastOnGround = lastOnGround;
            if (!lastLastOnGround && !lastOnGround && !onGround && !player.isInWater()) {
                double deltaY = (currentLoc.getY() - lastLocation.getY());
                double lastDeltaY = this.lastDeltaY;
                this.lastDeltaY = deltaY;
                double predictedDeltaY = (lastDeltaY - 0.08) * 0.9800000190734863D;
                double result = currentLoc.getY() - lastLocation.getY() - predictedDeltaY;

                double lastResult = this.lastResult;
                this.lastResult = result;

                if (result > 0.1 || (lastResult == 0.0784000015258789 && result == 0.0784000015258789)) {
                    if (++buffer > 2) {
                        fail("lastResult=" + lastResult + " result=" + result + " predictedDeltaY=" + predictedDeltaY + " deltaY=" + String.valueOf(currentLoc.getY() - lastLocation.getY()), player);
                    }
                } else buffer -= buffer > 0 ? 1 : 0;
            }
        }
    }

    public boolean isNearGround(Location location) {
        double expand = 0.3;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (location.clone().add(x, -0.5001, z).getBlock().getType() != Material.AIR)
                    return true;
            }
        }
        return false;
    }

    //Not at all taken from Juaga Anticheat, ignore this message ;)
    //Got stuck with a bug, had most of this code myself anyway
}
