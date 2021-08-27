package com.justdoom.flappyanticheat.checks.movement.speed;

import com.justdoom.flappyanticheat.checks.Check;
import com.justdoom.flappyanticheat.checks.CheckInfo;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import com.justdoom.flappyanticheat.packet.Packet;
import com.justdoom.flappyanticheat.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;

@CheckInfo(check = "Speed", checkType = "A", experimental = false, description = "Fly")
public class SpeedA extends Check{

    public SpeedA(FlappyPlayer data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        /**if (packet.isPosition()) {

            //first one is obvious why we return, second is because this check falses with my check for an air block below
            if (data.getPlayer().isFlying()/** || data.getPositionProcessor().isOnWallOrFence()**) return;

            //yeah, this is from the jonhan tutorial
            double predicted = (data.getPositionProcessor().getLastDeltaY() - 0.08) * 0.9800000190734863;

            //declare our fenceBelow boolean
            boolean fenceBelow = false;

            //check if the player has a fence/wall below them. can cause falses, wont deal with it.
            for (Block block : PlayerUtil.getNearbyBlocks(new Location(data.getPlayer().getWorld(), data.getPlayer().getLocation().getX(),
                    data.getPlayer().getLocation().getY() - 2, data.getPlayer().getLocation().getZ()), 1, 0, 1)) {
                if ((block.getType().toString().contains("FENCE")) || (block.getType().toString().contains("WALL"))) {
                    fenceBelow = true;
                }
            }

            //cleans up code. Near_Vehicle should be active to account for boats, but in my current base it doesnt work +
            //disables the check.
            boolean exempt = isExempt(ExemptType.LIQUID, ExemptType.INSIDE_VEHICLE, //ExemptType.NEAR_VEHICLE,
                    ExemptType.CLIMBABLE, ExemptType.PLACING, ExemptType.PISTON);

            //Check if the players deltaY is off from the predicted, theyre in the air, no fence below (can false due to
            //how i get if theyre in air), and are exempt from the things above
            if ((Math.abs(data.getPositionProcessor().getDeltaY() - predicted) > 0.001) &&
                    data.getPositionProcessor().getAirTicks() > 6 && !fenceBelow && !exempt) {
                if (!data.getVelocityProcessor().isTakingVelocity()) {
                    fail("1 exp" + predicted + " got=" + data.getPositionProcessor().getDeltaY());
                    //handle lag backing
                    if (now() - lastLagBackMS > 40) {
                        //data.getPlayer().teleport(data.getLagBackProcessor().getLagBackLocation());
                        lastLagBackMS = now();
                    }
                }

                double maxDeltaY = predicted + (data.getVelocityProcessor().isTakingVelocity() ?
                        data.getVelocityProcessor().getVelocityY() > 0 ? data.getVelocityProcessor().getVelocityY() : 0 : 0);

                if (maxDeltaY < data.getPositionProcessor().getDeltaY()) {
                    fail("mdy=" + maxDeltaY + " dy=" + data.getPositionProcessor().getDeltaY() + " vy=" +
                            data.getVelocityProcessor().getVelocityY());
                }

                //
                // Handle slow falls/glides
                //

                //make sure the player is off the ground and check if theyre previous going down/stable
                if (data.getPositionProcessor().getAirTicks() > 0 && data.getPositionProcessor().getLastDeltaY() <= 0) {
                    //i believe the lowest deltaY you can go is -3.92
                    if (predicted < -3.92) {
                        predicted = -3.92;
                    }

                    //establish our accuracy. compare out deltaY to what we predicted
                    double accuracy = predicted - data.getPositionProcessor().getDeltaY();

                    //check if the deltaY is within a 0.001 margin of our prediction
                    if (accuracy > 0.001) {
                        fail("exp=" + predicted + " got=" + data.getPositionProcessor().getDeltaY() +
                                " acc=" + accuracy + " at=" +
                                data.getPositionProcessor().getAirTicks());
                        **if (now() - lastLagBackMS > 40) {
                            data.getPlayer().teleport(data.getLagBackProcessor().getLagBackLocation());
                            lastLagBackMS = now();
                        }**
                    }
                }
            }
        }**/
    }
}