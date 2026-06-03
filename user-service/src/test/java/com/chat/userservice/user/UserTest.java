package com.chat.userservice.user;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void createUserWithIdUsernameAndDisplayname() {
        User user = new User(1L, "test_king", "King_of_Tests");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("test_king");
        assertThat(user.getDisplayname()).isEqualTo("King_of_Tests");
    }
}
