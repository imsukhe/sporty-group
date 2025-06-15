package com.sporty.jackpot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Jackpot extends Auditable {

    private String id;
    private double initialAmount;
    private double currentAmount;
    private String contributionType;
    private String rewardType;

    public Jackpot(String id, double initialAmount, String contributionType, String rewardType) {
        this(id, initialAmount, initialAmount, contributionType, rewardType);
    }

    public void reset() {
        this.currentAmount = initialAmount;
    }

    public void addToPool(double amount) {
        this.currentAmount += amount;
    }

}