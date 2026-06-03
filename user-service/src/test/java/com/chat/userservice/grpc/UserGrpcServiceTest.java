package com.chat.userservice.grpc;

import com.chat.grpc.GetUserRequest;
import com.chat.grpc.UserProfile;
import com.chat.userservice.user.User;
import com.chat.userservice.user.UserResponse;
import com.chat.userservice.user.UserService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserGrpcServiceTest {

    private final UserService userService = mock(UserService.class);
    private final UserGrpcService grpcService = new UserGrpcService(userService);

    @Test
    void getUserReturnsProfile() {
        when(userService.getById(1L))
                .thenReturn(new User(1L, "test_king", "King_of_Tests"));

        @SuppressWarnings("unchecked")
        StreamObserver<UserProfile> observer = mock(StreamObserver.class);

        grpcService.getUser(GetUserRequest.newBuilder().setId(1L).build(), observer);

        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(observer).onNext(captor.capture());
        verify(observer).onCompleted();

        UserProfile profile = captor.getValue();
        assertThat(profile.getId()).isEqualTo(1L);
        assertThat(profile.getUsername()).isEqualTo("test_king");
        assertThat(profile.getDisplayname()).isEqualTo("King_of_Tests");
    }
}
