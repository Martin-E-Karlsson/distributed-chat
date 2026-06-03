package com.chat.authservice.auth;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.chat.authservice.credential.Credential;
import com.chat.authservice.credential.CredentialService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CredentialService credentialService;

    public AuthController(AuthService authService, CredentialService credentialService) {
        this.authService = authService;
        this.credentialService = credentialService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        Credential createdCredential = credentialService.register(request.username(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(RegisterResponse.from(createdCredential));
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.username(), request.password());
        return new TokenResponse(token);
    }
}
