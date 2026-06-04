package com.chat.messageservice.message;

import java.time.Instant;

public record MessageResponse(
        Long id, Long senderId, String content, Instant createdAt, String senderUsername) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(), message.getSenderId(), message.getContent(),
                message.getCreatedAt(), null);
    }

    public static MessageResponse from(MessageWithSender withSender) {
        Message message = withSender.message();
        return new MessageResponse(
                message.getId(), message.getSenderId(), message.getContent(),
                message.getCreatedAt(), withSender.senderUsername());
    }
}