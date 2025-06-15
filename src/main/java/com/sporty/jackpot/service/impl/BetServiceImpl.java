package com.sporty.jackpot.service.impl;

import com.sporty.jackpot.db.InMemoryDb;
import com.sporty.jackpot.model.Jackpot;
import com.sporty.jackpot.model.JackpotContribution;
import com.sporty.jackpot.model.JackpotReward;
import com.sporty.jackpot.pojo.request.BetRequest;
import com.sporty.jackpot.pojo.response.EvaluationResponse;
import com.sporty.jackpot.service.BetService;
import com.sporty.jackpot.service.kafka.KafkaProducerService;
import com.sporty.jackpot.factory.RewardStrategyFactory;
import com.sporty.jackpot.strategy.RewardStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {

    private final KafkaProducerService kafkaProducer;

    private final InMemoryDb db = InMemoryDb.getInstance();

    @Override
    public String publishBet(BetRequest betRequest) {
        log.info("Publishing bet to Kafka: {}", betRequest);
        kafkaProducer.send("jackpot-bets", betRequest);
        return "Bet published to Kafka";
    }

    @Override
    public EvaluationResponse evaluateBet(String betId) {
        log.info("Evaluating bet for jackpot: {}", betId);

        JackpotContribution contribution = db.getContributionByBetId(betId);
        if (contribution == null) {
            log.error("No contribution found for bet ID: {}", betId);
            return new EvaluationResponse(false, 0);
        }

        Jackpot jackpot = db.getJackpotById(contribution.getJackpotId());
        if (jackpot == null) {
            log.error("No jackpot found with ID: {}", contribution.getJackpotId());
            return new EvaluationResponse(false, 0);
        }

        RewardStrategy strategy = RewardStrategyFactory.getStrategy(jackpot.getRewardType());
        boolean isWinner = strategy.isWinner(jackpot);
        double rewardAmount = isWinner ? jackpot.getCurrentAmount() : 0;

        if (isWinner) {
            JackpotReward reward = new JackpotReward(
                    contribution.getBetId(),
                    contribution.getUserId(),
                    contribution.getJackpotId(),
                    rewardAmount
            );
            db.saveReward(reward);
            jackpot.reset();
            log.info("Bet {} is a winner! Reward: {}", betId, rewardAmount);
        } else {
            log.info("Bet {} did not win the jackpot.", betId);
        }

        return new EvaluationResponse(isWinner, rewardAmount);
    }

}
