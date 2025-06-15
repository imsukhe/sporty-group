package com.sporty.jackpot.service.bet;

import com.sporty.jackpot.db.InMemoryDb;
import com.sporty.jackpot.model.Jackpot;
import com.sporty.jackpot.model.JackpotContribution;
import com.sporty.jackpot.pojo.request.BetRequest;
import com.sporty.jackpot.factory.ContributionStrategyFactory;
import com.sporty.jackpot.strategy.ContributionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BetProcessorService {

    private final InMemoryDb db = InMemoryDb.getInstance();

    public void processBet(BetRequest request) {
        log.info("Processing bet: {}", request);

        Jackpot jackpot = db.getJackpotById(request.getJackpotId());
        if (jackpot == null) {
            log.warn("No jackpot found for ID: {}", request.getJackpotId());
            return;
        }

        ContributionStrategy strategy = ContributionStrategyFactory.getStrategy(jackpot.getContributionType());
        double contributionAmount = strategy.calculateContribution(request.getAmount(), jackpot);
        jackpot.addToPool(contributionAmount);

        JackpotContribution contribution = new JackpotContribution(
                request.getBetId(),
                request.getUserId(),
                request.getJackpotId(),
                request.getAmount(),
                contributionAmount,
                jackpot.getCurrentAmount()
        );

        db.saveContribution(contribution);
        log.info("Saved contribution for betId: {}, amount: {}, new jackpot amount: {}",
                request.getBetId(), contributionAmount, jackpot.getCurrentAmount());
    }

}

