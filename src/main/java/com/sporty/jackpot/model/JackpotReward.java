package com.sporty.jackpot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JackpotReward extends Auditable {

    private String betId;
    private String userId;
    private String jackpotId;
    private double jackpotRewardAmount;

}