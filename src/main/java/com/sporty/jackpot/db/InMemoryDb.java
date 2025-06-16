package com.sporty.jackpot.db;

import com.sporty.jackpot.model.Jackpot;
import com.sporty.jackpot.model.JackpotContribution;
import com.sporty.jackpot.model.JackpotReward;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDb {

    private final Map<String, Jackpot> jackpots = new ConcurrentHashMap<>();
    private final Map<String, JackpotContribution> contributions = new ConcurrentHashMap<>();
    private final Map<String, JackpotReward> rewards = new ConcurrentHashMap<>();

    private InMemoryDb() {
    }

    private static class Holder {
        private static final InMemoryDb INSTANCE = new InMemoryDb();
    }

    public static InMemoryDb getInstance() {
        return Holder.INSTANCE;
    }

    public Jackpot getJackpotById(String id) {
        return jackpots.get(id);
    }

    public void addJackpot(Jackpot jackpot) {
        jackpots.put(jackpot.getId(), jackpot);
    }

    public void saveContribution(JackpotContribution contribution) {
        contributions.put(contribution.getBetId(), contribution);
    }

    public JackpotContribution getContributionByBetId(String betId) {
        return contributions.get(betId);
    }

    public void saveReward(JackpotReward reward) {
        rewards.put(reward.getBetId(), reward);
    }

    public JackpotReward getRewardByBetId(String betId) {
        return rewards.get(betId);
    }

    public Collection<Jackpot> getAllJackpots() {
        return jackpots.values();
    }

    public void clear() {
        jackpots.clear();
        contributions.clear();
        rewards.clear();
    }

}