package com.sporty.jackpot.strategy;

import com.sporty.jackpot.model.Jackpot;

public interface ContributionStrategy {

    double calculateContribution(double betAmount, Jackpot jackpot);

}