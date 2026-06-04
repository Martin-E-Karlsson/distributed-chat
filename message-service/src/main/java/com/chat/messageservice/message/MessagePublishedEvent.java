package com.chat.messageservice.message;

import java.time.Instant;

public record MessagePublishedEvent(Long messageId, Long senderId, String content, Instant createdAt) {

    public static MessagePublishedEvent from(Message message) {
        return new MessagePublishedEvent(
                message.getId(), message.getSenderId(), message.getContent(), message.getCreatedAt());
    }
}
