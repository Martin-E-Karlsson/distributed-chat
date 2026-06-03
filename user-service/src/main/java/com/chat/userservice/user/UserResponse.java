package com.chat.userservice.user;

public record UserResponse(Long id, String username, String displayname) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getDisplayname());
    }
}