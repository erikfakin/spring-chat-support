package com.erikfakin.springchatsupport.repositories;

import com.erikfakin.springchatsupport.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatroomId(UUID chatroomId);
    Long countByChatroomIdAndSenderAndSeenIsNull(UUID chatroomId, String sender);
    List<Message> findAllByChatroomIdAndSenderAndSeenIsNull(UUID chatroomId, String sender);
}
