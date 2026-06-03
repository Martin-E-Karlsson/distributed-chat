package com.chat.authservice.auth;

import com.chat.authservice.credential.Credential;
import com.chat.authservice.credential.CredentialService;
import com.chat.authservice.jwt.JwtService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private final CredentialService credentialService = mock(CredentialService.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final AuthService authService = new AuthService(credentialService, jwtService);

    @Test
    void loginReturnsTokenForValidCredentials() {
        when(credentialService.authenticate("test_king", "s3cret"))
                .thenReturn(Optional.of(new Credential(1L, "test_king", "hash")));
        when(jwtService.generateToken(1L, "test_king")).thenReturn("the-token");

        assertThat(authService.login("test_king", "s3cret")).isEqualTo("the-token");
    }

    @Test
    void loginThrowsForInvalidCredentials() {
        when(credentialService.authenticate("test_king", "wrong"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login("test_king", "wrong"))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
