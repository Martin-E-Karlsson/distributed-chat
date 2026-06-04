package com.chat.bff.message;

public record DownstreamMessageRequest(Long senderId, String content) {}