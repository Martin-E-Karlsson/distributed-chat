package com.chat.bff.user;

import com.chat.bff.auth.AuthClient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserBffController {

    private final AuthClient authClient;
    private final UserClient userClient;

    public UserBffController(AuthClient authClient, UserClient userClient) {
        this.authClient = authClient;
        this.userClient = userClient;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        authClient.register(request.username(), request.password());
        UserResponse profile = userClient.createUser(request.username(), request.displayname());
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }
}
