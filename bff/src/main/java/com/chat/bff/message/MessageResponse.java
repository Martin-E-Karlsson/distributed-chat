package com.chat.bff.message;

import java.time.Instant;

public record MessageResponse(
        Long id, Long senderId, String content, Instant createdAt, String senderUsername) {}