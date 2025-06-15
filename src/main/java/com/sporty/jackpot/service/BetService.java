package com.sporty.jackpot.service;

import com.sporty.jackpot.pojo.request.BetRequest;
import com.sporty.jackpot.pojo.response.EvaluationResponse;

public interface BetService {

    String publishBet(BetRequest betRequest);

    EvaluationResponse evaluateBet(String betId);

}