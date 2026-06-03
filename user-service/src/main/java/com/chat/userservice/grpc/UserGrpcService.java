package com.chat.userservice.grpc;

import com.chat.grpc.GetUserRequest;
import com.chat.grpc.UserProfile;
import com.chat.grpc.UserProfileServiceGrpc;
import com.chat.userservice.user.User;
import com.chat.userservice.user.UserService;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class UserGrpcService extends UserProfileServiceGrpc.UserProfileServiceImplBase {

    private final UserService userService;

    public UserGrpcService(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserProfile> responseObserver) {
        User user = userService.getById(request.getId());

        UserProfile profile = UserProfile.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setDisplayname(user.getDisplayname())
                .build();

        responseObserver.onNext(profile);
        responseObserver.onCompleted();
    }
}