package com.sporty.jackpot.controller;

import com.sporty.jackpot.pojo.request.BetRequest;
import com.sporty.jackpot.pojo.response.EvaluationResponse;
import com.sporty.jackpot.service.BetService;
import com.sporty.jackpot.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bets")
@RequiredArgsConstructor
public class BetController {

    private final KafkaProducerService kafkaProducer;

    private final BetService betService;

    @PostMapping
    public String publishBet(@RequestBody BetRequest betRequest) {
        kafkaProducer.send("jackpot-bets", betRequest);
        return "Bet published to Kafka";
    }

    @GetMapping("/{betId}/evaluate")
    public EvaluationResponse evaluateBet(@PathVariable String betId) {
        return betService.evaluateBet(betId);
    }

}