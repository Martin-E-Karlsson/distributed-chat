package com.chat.messageservice.event;

import com.chat.messageservice.message.MessageEventPublisher;
import com.chat.messageservice.message.MessagePublishedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class RabbitMessageEventPublisher implements MessageEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final JsonMapper jsonMapper;

    public RabbitMessageEventPublisher(RabbitTemplate rabbitTemplate, JsonMapper jsonMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void publish(MessagePublishedEvent event) {
        String json = jsonMapper.writeValueAsString(event);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, json);
    }
}
