package com.imjustdoom.flappyanticheat.data.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import com.imjustdoom.flappyanticheat.FlappyAnticheat;
import com.imjustdoom.flappyanticheat.data.FlappyPlayer;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class VelocityProcessor {

    private FlappyPlayer data;
    private double velocityX, velocityY, velocityZ, lastVelocityX, lastVelocityY, lastVelocityZ, velocityXZ, lastVelocityXZ;
    private boolean verifyingVelocity;
    private int maxVelocityTicks, velocityTicks;
    private short transactionID, velocityID;
    private long transactionPing, transactionReply;

    public VelocityProcessor(FlappyPlayer data){
        this.data = data;
    }

    public void handle(final double velocityX, final double velocityY, final double velocityZ){

        // Set last velocity
        this.lastVelocityX = this.velocityX;
        this.lastVelocityY = this.velocityY;
        this.lastVelocityZ = this.velocityZ;
        lastVelocityXZ = velocityXZ;

        // Set current velocity
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        velocityXZ = Math.hypot(velocityX, velocityZ);
    }

    public boolean isTakingVelocity() {
        return Math.abs(FlappyAnticheat.INSTANCE.getTickManager().getTicks() - this.velocityTicks) < this.maxVelocityTicks;
    }

    //TODO: velocity stuff
    public void handleTransaction(final WrapperPlayClientWindowConfirmation wrapper) {

        if (this.verifyingVelocity && wrapper.getActionId() == this.velocityID) {
            this.verifyingVelocity = false;
            this.velocityTicks = FlappyAnticheat.INSTANCE.getTickManager().getTicks();
            this.maxVelocityTicks = (int) (((lastVelocityZ + lastVelocityX) / 2 + 2) * 15);
        }

        if (wrapper.getActionId() == transactionID) {
            transactionPing = System.currentTimeMillis() - transactionReply;

            transactionID = (short) ThreadLocalRandom.current().nextInt(32767);
            PacketEvents.getAPI().getPlayerManager().sendPacket(data.getPlayer(), new WrapperPlayServerWindowConfirmation((byte) 0, transactionID, false));
            transactionReply = System.currentTimeMillis();
        }
    }
}
