package com.chat.messageservice.message;

public record MessageWithSender(Message message, String senderUsername) {}