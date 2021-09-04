package com.imjustdoom.api.check;

public interface FlappyCheck {

    CheckInfo getCheckInfo();

    int getMaxVl();

    int getVl();

    boolean isPunishable();

    boolean isLagbackable();
}
