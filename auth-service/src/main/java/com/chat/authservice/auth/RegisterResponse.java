package com.chat.authservice.auth;

import com.chat.authservice.credential.Credential;

public record RegisterResponse (Long id, String username) {

    public static RegisterResponse from(Credential credential) {
        return new RegisterResponse(credential.getId(), credential.getUsername());
    }
}
