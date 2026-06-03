package com.chat.userservice.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(String username, String displayname) {
        return userRepository.save(new User(null, username, displayname));
    }
}
