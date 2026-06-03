package com.chat.authservice.auth;

import com.chat.authservice.credential.Credential;
import com.chat.authservice.credential.CredentialService;
import com.chat.authservice.jwt.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CredentialService credentialService;
    private final JwtService jwtService;

    public AuthService(CredentialService credentialService, JwtService jwtService) {
        this.credentialService = credentialService;
        this.jwtService = jwtService;
    }

    public String login(String username, String rawPassword) {
        Credential credential = credentialService.authenticate(username, rawPassword)
                .orElseThrow(InvalidCredentialsException::new);
        return jwtService.generateToken(credential.getId(), credential.getUsername());
    }
}
