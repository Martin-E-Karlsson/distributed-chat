package com.chat.userservice.user;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void createUserWithIdUsernameAndDisplayName() {
        User user = new User(1L, "test_king", "King_of_Tests");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("test_king");
        assertThat(user.getDisplayName()).isEqualTo("King_of_Tests");
    }
}
