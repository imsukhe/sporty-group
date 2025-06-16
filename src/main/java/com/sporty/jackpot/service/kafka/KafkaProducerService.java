package com.sporty.jackpot.service.kafka;

import com.sporty.jackpot.pojo.request.BetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, BetRequest message) {
        kafkaTemplate.send(topic, message);
    }
}