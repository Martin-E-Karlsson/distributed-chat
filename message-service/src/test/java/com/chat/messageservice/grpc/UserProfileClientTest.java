package com.chat.messageservice.grpc;

import com.chat.grpc.GetUserRequest;
import com.chat.grpc.UserProfile;
import com.chat.grpc.UserProfileServiceGrpc;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserProfileClientTest {

    private final UserProfileServiceGrpc.UserProfileServiceBlockingStub stub =
            mock(UserProfileServiceGrpc.UserProfileServiceBlockingStub.class);
    private final UserProfileClient userProfileClient = new UserProfileClient(stub);

    @Test
    void getProfileFetchesUserById() {
        when(stub.getUser(GetUserRequest.newBuilder().setId(42L).build()))
                .thenReturn(UserProfile.newBuilder()
                        .setId(42L)
                        .setUsername("test_king")
                        .setDisplayname("King_of_Tests")
                        .build());

        UserProfile profile = userProfileClient.getProfile(42L);

        assertThat(profile.getUsername()).isEqualTo("test_king");
        assertThat(profile.getDisplayname()).isEqualTo("King_of_Tests");
    }
}