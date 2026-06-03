package com.chat.userservice.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void createAndReturnSavedUser() {

        when(userRepository.save(any(User.class))).
                thenReturn(new User(1L, "test_king", "King_of_Tests"));

        User createdUser = userService.create("test_king", "King_ofTests");

        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser.getUserName()).isEqualTo("test_king");
        verify(userRepository).save(any(User.class));
    }
}
