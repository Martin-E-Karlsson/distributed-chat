package com.chat.messageservice.message;

import com.chat.messageservice.grpc.UserProfileClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageQueryService {

    private final MessageService messageService;
    private final UserProfileClient userProfileClient;

    public MessageQueryService(MessageService messageService, UserProfileClient userProfileClient) {
        this.messageService = messageService;
        this.userProfileClient = userProfileClient;
    }

    public List<MessageWithSender> findAllWithSender() {
        return messageService.findAll().stream()
                .map(message -> new MessageWithSender(message, resolveUsername(message.getSenderId())))
                .toList();
    }

    private String resolveUsername(Long senderId) {
        try {
            return userProfileClient.getProfile(senderId).getUsername();
        } catch (RuntimeException ex) {
            return null;
        }
    }
}
