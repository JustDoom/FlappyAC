package com.imjustdoom.api.check;

public interface FlappyCheck {

    CheckInfo getCheckInfo();

    int getMaxVl();
    void setMaxVl(int maxVl);

    int getVl();
    void setVl(int vl);

    boolean isPunishable();

    boolean isLagbackable();
}
