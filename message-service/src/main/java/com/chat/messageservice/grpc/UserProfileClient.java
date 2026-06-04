package com.chat.messageservice.grpc;

import com.chat.grpc.GetUserRequest;
import com.chat.grpc.UserProfile;
import com.chat.grpc.UserProfileServiceGrpc;
import org.springframework.stereotype.Component;

@Component
public class UserProfileClient {

    private final UserProfileServiceGrpc.UserProfileServiceBlockingStub stub;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public UserProfileClient(UserProfileServiceGrpc.UserProfileServiceBlockingStub stub) {
        this.stub = stub;
    }

    public UserProfile getProfile(Long userId) {
        return stub.getUser(GetUserRequest.newBuilder().setId(userId).build());
    }
}