package com.sporty.jackpot.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BetRequest {

    private String betId;
    private String userId;
    private String jackpotId;
    private double amount;

    @Override
    public String toString() {
        return "BetRequest{" +
                "betId='" + betId + '\'' +
                ", userId='" + userId + '\'' +
                ", jackpotId='" + jackpotId + '\'' +
                ", amount=" + amount +
                '}';
    }

}