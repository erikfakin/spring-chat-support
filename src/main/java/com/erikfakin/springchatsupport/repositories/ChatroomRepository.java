package com.erikfakin.springchatsupport.repositories;

import com.erikfakin.springchatsupport.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {
    Chatroom findBySessionId(String sessionId);
    List<Chatroom> findAllByStatus(Chatroom.Status status);
}
