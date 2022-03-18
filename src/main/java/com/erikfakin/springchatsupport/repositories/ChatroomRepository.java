package com.erikfakin.springchatsupport.repositories;

import com.erikfakin.springchatsupport.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {
}
