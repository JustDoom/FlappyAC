package com.justdoom.flappyanticheat.checks.movement.noslow;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckData;
import com.justdoom.flappyanticheat.data.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.helditemslot.WrappedPacketOutHeldItemSlot;
import org.bukkit.Location;

@CheckData(name = "Fly", type = "A", experimental = false)
public class NoSlowA extends Check {

    private int hitTicks;
    private double lastDist;
    private Location lastLocation;

    public NoSlowA(){
        super("NoSlow", "A", true);
    }

    //doesnt work

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if(event.getPacketId() == PacketType.Play.Client.POSITION){
            WrappedPacketInFlying packet = new WrappedPacketInFlying(event.getNMSPacket());

            String path = ("checks." + check + "." + checkType).toLowerCase();
            if(PacketEvents.get().getServerUtils().getTPS() <= FlappyAnticheat.getInstance().getConfig().getDouble(path + ".min-tps")){
                return;
            }

            Location currentLoc = new Location(event.getPlayer().getWorld(), packet.getX(), packet.getY(), packet.getZ());
            Location lastLocation = this.lastLocation;
            this.lastLocation = currentLoc;

            double dist = test(lastLocation, currentLoc);
            double lastDist = this.lastDist;
            this.lastDist = dist;

            if(event.getPlayer().isSprinting() && ++hitTicks <= 2){
                double accel = Math.abs(dist - lastDist);
                //event.getPlayer().sendMessage(String.valueOf(accel));
                if(accel < 0.027){
                    //event.getPlayer().sendMessage("YEEEEEEEEEEEES");
                    //fail("test", event.getPlayer());
                }
            }

        } else if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(event.getNMSPacket());
            hitTicks = 0;
        }
    }

    public double test(Location from, Location to){
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }
}
