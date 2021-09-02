package com.justdoom.flappyanticheat.exempt;

import com.justdoom.flappyanticheat.data.FlappyPlayer;
import com.justdoom.flappyanticheat.exempt.type.ExemptType;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class ExemptManager {
    private final FlappyPlayer playerData;

    public boolean isExempt(final ExemptType exceptType) {
        return exceptType.getException().apply(playerData);
    }

    public boolean isExempt(final ExemptType... exemptTypes) {
        for (final ExemptType exemptType : exemptTypes) {
            if (this.isExempt(exemptType)) {
                return true;
            }
        }

        return false;
    }

    public boolean isExempt(final Function<FlappyPlayer, Boolean> exception) {
        return exception.apply(playerData);
    }
}