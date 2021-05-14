package com.justdoom.flappyanticheat.checks.movement.fly;

import com.justdoom.flappyanticheat.checks.Check;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyA extends Check implements Listener {

    private float buffer;
    private double lastResult, lastDeltaY;
    private boolean lastLastOnGround, lastOnGround;

    public FlyA() {
        super("Fly", "A", true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getFrom().getX() == event.getTo().getX()
                && event.getFrom().getY() == event.getTo().getY()
                && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }

        boolean onGround = isNearGround(event.getTo());
        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;
        boolean lastLastOnGround = this.lastLastOnGround;
        this.lastLastOnGround = lastOnGround;
        if (!lastLastOnGround && !lastOnGround && !onGround) {
            double deltaY = (event.getTo().getY() - event.getFrom().getY());
            double lastDeltaY = this.lastDeltaY;
            this.lastDeltaY = deltaY;
            double predictedDeltaY = (lastDeltaY - 0.08) * 0.9800000190734863D;
            double result = event.getTo().getY() - event.getFrom().getY() - predictedDeltaY;

            double lastResult = this.lastResult;
            this.lastResult = result;

            if (result > 0.1 || (lastResult == 0.0784000015258789 && result == 0.0784000015258789)) {
                if (++buffer > 2) {
                    fail("lastResult=" + lastResult + " result=" + result + " predictedDeltaY=" + predictedDeltaY + " deltaY=" + String.valueOf(event.getTo().getY() - event.getFrom().getY()), player);
                }
            } else buffer -= buffer > 0 ? 1 : 0;
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

    //Not at all taken from Juaga, ignore this message ;)
    //Got stuck with a bug
}
