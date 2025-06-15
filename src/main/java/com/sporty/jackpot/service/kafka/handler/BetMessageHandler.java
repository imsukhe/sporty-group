package com.sporty.jackpot.service.kafka.handler;

import com.sporty.jackpot.pojo.request.BetRequest;
import com.sporty.jackpot.service.bet.BetProcessorService;
import com.sporty.jackpot.service.kafka.KafkaHandlerRegistry;
import com.sporty.jackpot.service.kafka.KafkaMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BetMessageHandler implements KafkaMessageHandler<BetRequest> {

    private final KafkaHandlerRegistry registry;
    private final BetProcessorService processor;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        registry.register("jackpot-bets", this);
        log.info("Registered BetMessageHandler for topic: jackpot-bets");
    }

    @Override
    public void handle(BetRequest message) {
        log.info("Received BetRequest for processing: {}", message);
        processor.processBet(message);
    }

}