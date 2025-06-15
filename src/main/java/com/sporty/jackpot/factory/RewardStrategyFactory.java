package com.sporty.jackpot.factory;


import com.sporty.jackpot.strategy.RewardStrategy;

import java.util.Random;

public class RewardStrategyFactory {

    private static final Random random = new Random();

    public static RewardStrategy getStrategy(String type) {
        return switch (type.toUpperCase()) {
            case "FIXED" -> jackpot -> random.nextDouble() < 0.01;
            case "VARIABLE" -> jackpot -> {
                double current = jackpot.getCurrentAmount();
                double initial = jackpot.getInitialAmount();
                double growthRatio = Math.min(current / (initial * 10), 1.0);
                return random.nextDouble() < growthRatio;
            };
            default -> throw new IllegalArgumentException("Unsupported reward type: " + type);
        };
    }

}