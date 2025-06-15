package com.sporty.jackpot.strategy;

import com.sporty.jackpot.model.Jackpot;

public interface RewardStrategy {

    boolean isWinner(Jackpot jackpot);

}