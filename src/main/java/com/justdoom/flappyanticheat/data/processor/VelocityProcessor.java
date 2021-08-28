package com.justdoom.flappyanticheat.data.processor;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import com.justdoom.flappyanticheat.data.FlappyPlayer;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class VelocityProcessor {

    private FlappyPlayer data;

    public VelocityProcessor(FlappyPlayer data){
        this.data = data;
    }

    private double velocityX, velocityY, velocityZ, lastVelocityX, lastVelocityY, lastVelocityZ;
    private boolean verifyingVelocity;
    private int maxVelocityTicks, velocityTicks;
    private short transactionID, velocityID;
    private long transactionPing, transactionReply;

    public void handle(final double velocityX, final double velocityY, final double velocityZ){
        this.lastVelocityX = this.velocityX;
        this.lastVelocityY = this.velocityY;
        this.lastVelocityZ = this.velocityZ;

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    public boolean isTakingVelocity() {
        return Math.abs(FlappyAnticheat.INSTANCE.getTickManager().getTicks() - this.velocityTicks) < this.maxVelocityTicks;
    }

    public void handleTransaction(final WrappedPacketInTransaction wrapper) {

        if (this.verifyingVelocity && wrapper.getActionNumber() == this.velocityID) {
            this.verifyingVelocity = false;
            this.velocityTicks = FlappyAnticheat.INSTANCE.getTickManager().getTicks();
            this.maxVelocityTicks = (int) (((lastVelocityZ + lastVelocityX) / 2 + 2) * 15);
        }

        if (wrapper.getActionNumber() == transactionID) {
            transactionPing = System.currentTimeMillis() - transactionReply;

            transactionID = (short) ThreadLocalRandom.current().nextInt(32767);
            PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(), new WrappedPacketOutTransaction(0, transactionID, false));
            transactionReply = System.currentTimeMillis();
        }
    }
}
