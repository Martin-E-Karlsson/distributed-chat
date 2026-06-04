package com.chat.messageservice.message;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService {

    private final  MessageRepository messageRepository;
    private final MessageEventPublisher eventPublisher;

    public MessageService(MessageRepository messageRepository,  MessageEventPublisher eventPublisher) {
        this.messageRepository = messageRepository;
        this.eventPublisher = eventPublisher;
    }

    public Message create(Long senderId, String content) {
        Message savedMessage = messageRepository.save(new Message(null, senderId, content, Instant.now()));
        eventPublisher.publish(MessagePublishedEvent.from(savedMessage));
        return savedMessage;
    }

    public List<Message> findAll() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }
}
