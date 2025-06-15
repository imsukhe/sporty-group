package com.sporty.jackpot.service.kafka;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaHandlerRegistry {

    private final Map<String, KafkaMessageHandler<?>> handlers = new ConcurrentHashMap<>();

    public <T> void register(String topic, KafkaMessageHandler<T> handler) {
        handlers.put(topic, handler);
    }

    @SuppressWarnings("unchecked")
    public <T> KafkaMessageHandler<T> getHandler(String topic) {
        return (KafkaMessageHandler<T>) handlers.get(topic);
    }

}
