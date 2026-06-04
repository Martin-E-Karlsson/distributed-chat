package com.chat.bff.message;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageBffController {

    private final MessageClient messageClient;

    public MessageBffController(MessageClient messageClient) {
        this.messageClient = messageClient;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> send(
            @Valid @RequestBody SendMessageRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        Long senderId = Long.valueOf(jwt.getSubject()); // The subject of the jwt is the sender ID
        MessageResponse created = messageClient.sendMessage(senderId, request.content());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<MessageResponse> all() {
        return messageClient.getMessages();
    }
}
