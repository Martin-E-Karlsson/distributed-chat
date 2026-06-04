package com.chat.bff.user;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient userRestClient;

    public UserClient(RestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    public UserResponse createUser(String username, String displayname) {
        return userRestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UserProfileRequest(username, displayname))
                .retrieve()
                .body(UserResponse.class);
    }
}
