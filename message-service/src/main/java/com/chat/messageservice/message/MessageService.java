package com.chat.messageservice.message;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService {

    private final  MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message create(Long senderId, String content) {
        return messageRepository.save(new Message(null, senderId, content, Instant.now()));
    }

    public List<Message> findAll() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }
}
