package com.chat.bff.auth;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

    private final RestClient authRestClient;

    public AuthClient(RestClient authRestClient) {
        this.authRestClient = authRestClient;
    }

    public TokenResponse login(LoginRequest request) {
        return authRestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(TokenResponse.class);
    }

    public void register(String username, String password) {
        authRestClient.post()
                .uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Credentials(username, password))
                .retrieve()
                .toBodilessEntity();
    }
}
