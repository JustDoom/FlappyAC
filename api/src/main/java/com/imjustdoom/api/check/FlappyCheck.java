package com.imjustdoom.api.check;

public interface FlappyCheck {

    CheckInfo getCheckInfo();

    int getMaxVl();
    int setMaxVl(int maxVl);

    int getVl();
    int setVl(int vl);

    boolean isPunishable();

    boolean isLagbackable();
}
