package com.sporty.jackpot.service.kafka.handler;

import com.sporty.jackpot.pojo.request.BetRequest;
import com.sporty.jackpot.service.bet.BetProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BetMessageHandler {

    private final BetProcessorService processor;

    @KafkaListener(topics = "jackpot-bets", groupId = "jackpot-group")
    public void handle(BetRequest message) {
        log.info("Received BetRequest for processing: {}", message);
        processor.processBet(message);
    }

}