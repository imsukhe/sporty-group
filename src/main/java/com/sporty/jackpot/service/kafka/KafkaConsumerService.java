package com.sporty.jackpot.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final KafkaHandlerRegistry registry;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "#{'${app.kafka.topics}'.split(',')}", groupId = "jackpot-group")
    public void consumeRaw(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        String json = record.value();

        try {
            var handler = registry.getHandler(topic);
            if (handler != null) {
                Class<?> messageType = handler.getClass()
                        .getDeclaredMethods()[0]
                        .getParameterTypes()[0];
                Object message = objectMapper.readValue(json, messageType);
                handler.handle(message);
            } else {
                System.err.println("No handler registered for topic: " + topic);
            }
        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
        }
    }

}
