package com.chat.bff.message;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class MessageClient {

    private final RestClient messageRestClient;

    public MessageClient(RestClient messageRestClient) {
        this.messageRestClient = messageRestClient;
    }

    public MessageResponse sendMessage(Long senderId, String content) {
        return messageRestClient.post()
                .uri("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new DownstreamMessageRequest(senderId, content))
                .retrieve()
                .body(MessageResponse.class);
    }

    public List<MessageResponse> getMessages() {
        return messageRestClient.get()
                .uri("/messages")
                .retrieve()
                .body(new ParameterizedTypeReference<List<MessageResponse>>() {});
    }
}
