package com.erikfakin.springchatsupport.repositories;

import com.erikfakin.springchatsupport.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
