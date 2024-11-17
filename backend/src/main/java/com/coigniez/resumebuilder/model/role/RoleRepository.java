package com.coigniez.resumebuilder.model.role;

import java.util.Optional;

public interface RoleRepository{
    Optional<Role> findByName(String roleStudent);
}
