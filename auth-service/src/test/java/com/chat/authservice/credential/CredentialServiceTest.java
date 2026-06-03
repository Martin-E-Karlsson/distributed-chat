package com.chat.authservice.credential;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

class CredentialServiceTest {

    private final CredentialRepository credentialRepository = mock(CredentialRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final CredentialService credentialService =
            new CredentialService(credentialRepository, passwordEncoder);

    @Test
    void registerStoresHashedPassword() {
        when(credentialRepository.existsByUsername("test_king")).thenReturn(false);
        when(credentialRepository.save(any(Credential.class)))
                .thenAnswer(invocation ->  invocation.getArgument(0));

        credentialService.register("test_king", "s3cret");

        ArgumentCaptor<Credential> captor = ArgumentCaptor.forClass(Credential.class);
        verify(credentialRepository).save(captor.capture());
        Credential savedCredential = captor.getValue();

        assertThat(savedCredential.getPasswordHash()).isNotEqualTo("s3cret");
        assertThat(passwordEncoder.matches("s3cret", savedCredential.getPasswordHash())).isTrue();
    }

    @Test
    void registerRejectsDuplicateUsername() {
        when(credentialRepository.existsByUsername("test_king")).thenReturn(true);

        assertThatThrownBy(() -> credentialService.register("test_king", "s3cret"))
                .isInstanceOf(DuplicateUsernameException.class);

        verify(credentialRepository, never()).save(any());
    }

    @Test
    void verifyReturnsTrueForCorrectPassword() {
        String hashedPassword = passwordEncoder.encode("s3cret");
        when(credentialRepository.findByUsername("test_king"))
                .thenReturn(Optional.of(new Credential(1L, "test_king", hashedPassword)));

        assertThat(credentialService.verify("test_king", "s3cret")).isTrue();
    }

    @Test
    void verifyReturnsFalseForWrongPassword() {
        String hashedPassword = passwordEncoder.encode("s3cret");
        when(credentialRepository.findByUsername("test_king"))
                .thenReturn(Optional.of((new Credential(1L, "test_king", hashedPassword))));

        assertThat(credentialService.verify("test_king", "wrong")).isFalse();
    }

    @Test
    void verifyReturnsFalseForUnknownUser() {
        when(credentialRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThat(credentialService.verify("ghost", "whatever")).isFalse();
    }

    @Test
    void authenticateReturnsCredentialForValidLogin() {
        String hashedPassword = passwordEncoder.encode("s3cret");
        when(credentialRepository.findByUsername("test_king"))
                .thenReturn(Optional.of(new Credential(1L, "test_king", hashedPassword)));

        assertThat(credentialService.authenticate("test_king", "s3cret"))
                .map(Credential::getId).contains(1L);
    }

    @Test
    void authenticateReturnsEmptyForInvalidLogin() {
        String hashedPassword = passwordEncoder.encode("s3cret");
        when(credentialRepository.findByUsername("test_king"))
                .thenReturn(Optional.of(new Credential(1L, "test_king", hashedPassword)));

        assertThat(credentialService.authenticate("test_king", "wrong")).isEmpty();
    }
}
