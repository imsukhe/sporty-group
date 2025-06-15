package com.sporty.jackpot.service.kafka;

public interface KafkaMessageHandler<T> {

    void handle(T message);

}
