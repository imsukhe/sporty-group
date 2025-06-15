package com.sporty.jackpot.factory;

import com.sporty.jackpot.strategy.ContributionStrategy;

public class ContributionStrategyFactory {

    public static ContributionStrategy getStrategy(String type) {
        return switch (type.toUpperCase()) {
            case "FIXED" -> (betAmount, jackpot) -> betAmount * 0.1;
            case "VARIABLE" -> (betAmount, jackpot) -> {
                double current = jackpot.getCurrentAmount();
                double initial = jackpot.getInitialAmount();
                double reductionFactor = Math.min(current / (initial * 10), 1.0);
                return betAmount * 0.1 * (1 - reductionFactor);
            };
            default -> throw new IllegalArgumentException("Unsupported contribution type: " + type);
        };
    }

}
