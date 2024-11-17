package com.coigniez.resumebuilder.model.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String username);
}
