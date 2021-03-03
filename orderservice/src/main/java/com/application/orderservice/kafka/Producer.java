package com.application.orderservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.application.orderservice.Event.OrderEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(OrderEvent orderEvent) {
        log.debug("Publishing event {}, order details are {}", orderEvent.getClass(), orderEvent.getOrder());
        this.kafkaTemplate.send(TopicConfig.TOPIC, orderEvent.toString());
    }

  /*  @Bean
    public NewTopic createTopic() {
        return new NewTopic(TopicConfig.TOPIC, 3, (short) 1);
    }*/

}
