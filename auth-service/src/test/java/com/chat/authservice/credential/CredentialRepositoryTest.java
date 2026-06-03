package com.chat.authservice.credential;

import com.zaxxer.hikari.util.Credentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CredentialRepositoryTest {

    @Autowired
    private CredentialRepository credentialRepository;

    @Test
    void savesAndFindsByUsername() {
        credentialRepository.save(new Credential(null, "test_king", "hashed-value"));

        Optional<Credential> foundCredential = credentialRepository.findByUsername("test_king");

        assertThat(foundCredential).isPresent();
        assertThat(foundCredential.get().getPasswordHash()).isEqualTo("hashed-value");
    }
}
