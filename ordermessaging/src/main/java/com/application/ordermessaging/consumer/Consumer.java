package com.application.ordermessaging.consumer;

import com.application.ordermessaging.topic.TopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Consumer {

    @KafkaListener(topics = TopicConfig.TOPIC, groupId = "group_id")
    public void consumeMessage(String event) {
        log.debug("Consuming event {}", event);
    }
}