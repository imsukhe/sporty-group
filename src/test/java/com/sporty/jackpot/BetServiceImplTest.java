package com.sporty.jackpot;

import com.sporty.jackpot.db.InMemoryDb;
import com.sporty.jackpot.factory.RewardStrategyFactory;
import com.sporty.jackpot.model.Jackpot;
import com.sporty.jackpot.model.JackpotContribution;
import com.sporty.jackpot.model.JackpotReward;
import com.sporty.jackpot.pojo.request.BetRequest;
import com.sporty.jackpot.pojo.response.EvaluationResponse;
import com.sporty.jackpot.service.impl.BetServiceImpl;
import com.sporty.jackpot.service.kafka.KafkaProducerService;
import com.sporty.jackpot.strategy.RewardStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BetServiceImplTest {

    @Mock
    private KafkaProducerService kafkaProducer;

    @Mock
    private RewardStrategy mockRewardStrategy;

    @InjectMocks
    private BetServiceImpl betService;

    private final InMemoryDb db = InMemoryDb.getInstance();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        db.clear();
    }

    @Test
    void publishBet_shouldSendBetToKafka() {
        BetRequest request = new BetRequest("bet123", "user1", "jackpot1", 100.0);
        String result = betService.publishBet(request);
        assertEquals("Bet published to Kafka", result);
        verify(kafkaProducer).send("jackpot-bets", request);
    }

    @Test
    void evaluateBet_shouldReturnLossIfContributionDoesNotExist() {
        EvaluationResponse response = betService.evaluateBet("nonexistent");
        assertFalse(response.isWinner());
        assertEquals(0, response.getRewardAmount());
    }

    @Test
    void evaluateBet_shouldReturnWinWhenStrategyReturnsTrue() {
        String betId = "bet001";
        Jackpot jackpot = new Jackpot("jackpot1", 1000.0, "FIXED", "FIXED");
        jackpot.addToPool(2000.0);
        db.addJackpot(jackpot);
        db.saveContribution(new JackpotContribution(betId, "user1", "jackpot1", 100, 10, 3000));

        when(mockRewardStrategy.isWinner(any())).thenReturn(true);

        try (MockedStatic<RewardStrategyFactory> mockStatic = mockStatic(RewardStrategyFactory.class)) {
            mockStatic.when(() -> RewardStrategyFactory.getStrategy("FIXED")).thenReturn(mockRewardStrategy);
            EvaluationResponse response = betService.evaluateBet(betId);

            assertTrue(response.isWinner());
            assertEquals(3000, response.getRewardAmount());

            JackpotReward reward = db.getRewardByBetId(betId);
            assertNotNull(reward);
            assertEquals(3000, reward.getJackpotRewardAmount());
        }
    }

    @Test
    void evaluateBet_shouldReturnLossWhenStrategyReturnsFalse() {
        String betId = "bet002";
        Jackpot jackpot = new Jackpot("jackpot2", 500.0, "FIXED", "FIXED");
        jackpot.addToPool(1000.0);
        db.addJackpot(jackpot);
        db.saveContribution(new JackpotContribution(betId, "user2", "jackpot2", 50, 5, 1500));

        when(mockRewardStrategy.isWinner(any())).thenReturn(false);

        try (MockedStatic<RewardStrategyFactory> mockStatic = mockStatic(RewardStrategyFactory.class)) {
            mockStatic.when(() -> RewardStrategyFactory.getStrategy("FIXED")).thenReturn(mockRewardStrategy);

            EvaluationResponse response = betService.evaluateBet(betId);

            assertFalse(response.isWinner());
            assertEquals(0, response.getRewardAmount());
            assertNull(db.getRewardByBetId(betId));
        }
    }

    @Test
    void evaluateBet_shouldResetJackpotOnWin() {
        String betId = "bet003";
        Jackpot jackpot = new Jackpot("jackpot3", 1000.0, "FIXED", "FIXED");
        jackpot.addToPool(3000.0);
        db.addJackpot(jackpot);
        db.saveContribution(new JackpotContribution(betId, "user3", "jackpot3", 150, 15, 4000));

        when(mockRewardStrategy.isWinner(any())).thenReturn(true);

        try (MockedStatic<RewardStrategyFactory> mockStatic = mockStatic(RewardStrategyFactory.class)) {
            mockStatic.when(() -> RewardStrategyFactory.getStrategy("FIXED")).thenReturn(mockRewardStrategy);

            betService.evaluateBet(betId);

            Jackpot updated = db.getJackpotById("jackpot3");
            assertEquals(1000.0, updated.getCurrentAmount());
        }
    }

}
