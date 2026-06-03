package com.chat.authservice.credential;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    public CredentialService(CredentialRepository credentialRepository,
                             PasswordEncoder passwordEncoder) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Credential register(String username, String rawPassword) {
        if (credentialRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }
        String passwordHash = passwordEncoder.encode(rawPassword);
        return credentialRepository.save(new Credential(null, username, passwordHash));
    }
}
