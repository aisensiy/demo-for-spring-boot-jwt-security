package com.example.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<User> getUserByUserName(String username);
    Optional<User> getUserById(String id);
}
