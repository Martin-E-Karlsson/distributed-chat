package com.chat.authservice.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private final JwtService jwtService =
            new JwtService("test-secret-test-secret-test-secret-1234", 3_600_000);

    @Test
    void generatesTokenWithUserIdSubjectAndUsernameClaim() {
        String token = jwtService.generateToken(42L, "test_king");

        Claims claims = jwtService.parse(token);

        assertThat(claims.getSubject()).isEqualTo("42");
        assertThat(claims.get("username", String.class)).isEqualTo("test_king");
    }
}
