package com.chat.userservice.user;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;

class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void createAndReturnSavedUser() {

        when(userRepository.save(any(User.class))).
                thenReturn(new User(1L, "test_king", "King_of_Tests"));

        User createdUser = userService.create("test_king", "King_of_Tests");

        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser.getUsername()).isEqualTo("test_king");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createRejectsDuplicateUsername() {
        when(userRepository.existsByUsername("test_king")).thenReturn(true);

        assertThatThrownBy(() -> userService.create("test_king", "King_of_Tests"))
                .isInstanceOf(DuplicateUsernameException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void getByIdReturnsUser() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User(1L, "test_king", "King_of_Tests")));
        
        User foundUser = userService.getById(1L);

        assertThat(foundUser.getUsername()).isEqualTo("test_king");
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
