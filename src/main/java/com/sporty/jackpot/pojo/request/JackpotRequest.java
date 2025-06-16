package com.sporty.jackpot.pojo.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JackpotRequest {

    private String id;
    private double initialAmount;
    private String contributionType;
    private String rewardType;

    @Override
    public String toString() {
        return "JackpotRequest{" +
                "id='" + id + '\'' +
                ", initialAmount=" + initialAmount +
                ", contributionType='" + contributionType + '\'' +
                ", rewardType='" + rewardType + '\'' +
                '}';
    }
}
