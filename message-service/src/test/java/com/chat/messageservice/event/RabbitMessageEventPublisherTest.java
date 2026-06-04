package com.chat.messageservice.event;

import com.chat.messageservice.message.MessagePublishedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RabbitMessageEventPublisherTest {

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private final JsonMapper jsonMapper = JsonMapper.builder().build();
    private final RabbitMessageEventPublisher publisher =
            new RabbitMessageEventPublisher(rabbitTemplate, jsonMapper);

    @Test
    void publishSendsJsonToExchangeWithRoutingKey() {
        MessagePublishedEvent event =
                new MessagePublishedEvent(7L, 42L, "Hello", Instant.parse("2026-01-01T00:00:00Z"));

        publisher.publish(event);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitConfig.EXCHANGE),
                eq(RabbitConfig.ROUTING_KEY),
                anyString());
    }
}
