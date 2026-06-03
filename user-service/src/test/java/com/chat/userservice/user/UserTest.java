package com.chat.userservice.user;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void createUserWithIdUsernameAndDisplayName() {
        User user = new User(1L, "Testony", "McTesterson");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("Testony");
        assertThat(user.getDisplayName()).isEqualTo("McTesterson");
    }
}
