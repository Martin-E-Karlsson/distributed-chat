package com.chat.messageservice.message;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody CreateMessageRequest request) {
        Message createdMessage = messageService.create(request.senderId(), request.content());
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageResponse.from(createdMessage));
    }

    @GetMapping
    public List<MessageResponse> all() {
        return messageService.findAll().stream()
                .map(MessageResponse::from)
                .toList();
    }
}
