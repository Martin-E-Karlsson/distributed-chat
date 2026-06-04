package com.chat.messageservice.message;

public interface MessageEventPublisher {

    void publish(MessagePublishedEvent event);
}
