package com.coigniez.resumebuilder.model.user;

import java.util.Optional;

public interface TokenRepository {

    Optional<Token> findByToken(String token);
}
