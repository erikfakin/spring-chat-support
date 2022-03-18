package com.erikfakin.springchatsupport.repositories;

import com.erikfakin.springchatsupport.entities.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    ClientUser findByEmail(String email);
}
